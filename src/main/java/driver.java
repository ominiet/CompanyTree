import org.apache.commons.text.similarity.FuzzyScore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;


public class driver {
    /**
     *
     * @param prop Properties object containing database username and password
     * @return  A list of all top level companies for bot risk match and AM Best
     */
    private static ArrayList<ArrayList<Long>> getAllTopLevelCompanies(Properties prop) {
        ArrayList<Long> topLevelRMCompanies = new ArrayList<>();
        ArrayList<Long> topLevelAMCompanies = new ArrayList<>();

        //Run SQL Queries
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/masterdb",
                    prop.getProperty("username"), prop.getProperty("password"));
            PreparedStatement pStatement = conn.prepareStatement("select COUNT(distinct name) as tree_size, ultimate_parent_amb_id from dom_am_best_carrier group by ultimate_parent_amb_id order by tree_size desc;");
            ResultSet rs = pStatement.executeQuery();


            while (rs.next()) {
                if (rs.getInt("ultimate_parent_amb_id") != 0)
                    topLevelAMCompanies.add(rs.getLong("ultimate_parent_amb_id"));
            }

            pStatement = conn.prepareStatement("select count(distinct name) as tree_size, top_level_company_id from dom_company group by top_level_company_id order by tree_size desc;");
            rs = pStatement.executeQuery();

            while (rs.next()) {
                if (rs.getLong("top_level_company_id") != 0)
                    topLevelRMCompanies.add(rs.getLong("top_level_company_id"));
            }
            conn.close();
        } catch (SQLException sq) {
            sq.printStackTrace();
        }


        //return sql query results
        ArrayList<ArrayList<Long>> result = new ArrayList<>(2);
        result.add(topLevelRMCompanies);
        result.add(topLevelAMCompanies);
        return result;
    }

    ///Search for an exact match of name string your set of risk match trees
    private static ArrayList<TreeNode> fuzzySearch(String search, ArrayList<CompanyTree> rm){
        ArrayList<TreeNode> resultOptions = new ArrayList<>();
        FuzzyScore score = new FuzzyScore(Locale.ENGLISH);

        int mark = (int)((double)score.fuzzyScore(search, search) * .30);

        ArrayList<FuzzyResult> matches = new ArrayList<>();

        for (CompanyTree rmTree : rm){

            for (TreeNode node: rmTree.getNodes()){
                int fScore = score.fuzzyScore(node.getName(), search);
                if (fScore > mark ){
                    matches.add(new FuzzyResult(node, score.fuzzyScore(node.getName(), search)));
                }
            }
        }

        matches.sort(Comparator.comparingInt(FuzzyResult::getResult).reversed());
        if (matches.size() > 20){
            matches.subList(19, matches.size()-1).clear();
        }

        for (FuzzyResult n : matches) {
            resultOptions.add(n.getNode());
        }

        return resultOptions;
    }




    public static void main(String[] args) {

        ArrayList<CompanyTree> rmTrees = new ArrayList<>();
        ArrayList<CompanyTree> amTrees = new ArrayList<>();
        Connection conn;

        Properties prop = new Properties();
        InputStream input;
        try {

            //get values from properties.configuration
            input = new FileInputStream("./Props/config.properties");
            prop.load(input);

            //get all top level names
            ArrayList<ArrayList<Long>> topLevelCompanyCodes = getAllTopLevelCompanies(prop);

            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/masterdb",
                            prop.getProperty("username"), prop.getProperty("password"));

            //Build Risk Match Trees
            PreparedStatement prepStatement = conn.prepareStatement("select dom_company.name, dom_company.id, parent_company_id, dom_company_role.name as role from dom_company left join dom_company_role on dom_company.primary_role_id = dom_company_role.id where top_level_company_id=? or dom_company.id = ?;");
            for (long rmCode : topLevelCompanyCodes.get(0)) {
                prepStatement.setString(1, Long.toString(rmCode));
                prepStatement.setString(2, Long.toString(rmCode));

                ResultSet rs = prepStatement.executeQuery();
                ArrayList<TreeNode> n = new ArrayList<>();
                CompanyTree tree1;

                try {

                    while (rs.next()) {
                        long id = rs.getLong("id");
                        String name = rs.getString("name");
                        long parentId = rs.getLong("parent_company_id");
                        String role = rs.getString("role");

                        //if rm Code is 0 you are dealing with null top level company (single node tree)
                        if (rmCode == 0) {
                            rmCode = id;
                        }
                        TreeNode temp = new TreeNode(id, name, parentId,role);
                        n.add(temp);
                    }
                    tree1 = new CompanyTree(n, rmCode);
                    rmTrees.add(tree1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Get all single node trees for Risk Match
            prepStatement = conn.prepareStatement("select dom_company.id, dom_company.name,dom_company.parent_company_id, dom_company_role.name as role from dom_company left join dom_company_role on dom_company.primary_role_id = dom_company_role.id left join (select distinct top_level_company_id as new_id from dom_company) as new_table on dom_company.id = new_id where new_table.new_id is null and dom_company.top_level_company_id is null;");
            ResultSet rs = prepStatement.executeQuery();

            while(rs.next()){
                long id = rs.getLong("id");
                String name = rs.getString("name");
                long parentId = rs.getLong("parent_company_id");
                String role = rs.getString("role");

                TreeNode temp = new TreeNode(id, name, parentId,role);
                CompanyTree tempTree = new CompanyTree(new ArrayList<>(Collections.singletonList(temp)), id);

                rmTrees.add(tempTree);

            }
            //Build AM Best Trees
            prepStatement = conn.prepareStatement("select amb_id as id, name, parent_amb_id as parent_company_id, ultimate_parent_amb_id as top_level_company_id from dom_am_best_carrier where ultimate_parent_amb_id = ? OR amb_id = ?;");
            for (long amCode : topLevelCompanyCodes.get(1)) {
                prepStatement.setString(1, Long.toString(amCode));
                prepStatement.setString(2, Long.toString(amCode));

                rs = prepStatement.executeQuery();
                ArrayList<TreeNode> n = new ArrayList<>();
                CompanyTree tree1;

                try {
                    while (rs.next()) {

                        long id = rs.getLong("id");
                        String name = rs.getString("name");
                        long parentId = rs.getLong("parent_company_id");

                        //Don't know why this is used but w/e
                        if (amCode == 0) {
                            amCode = id;
                        }
                        TreeNode temp = new TreeNode(id, name, parentId);
                        n.add(temp);

                    }

                    tree1 = new CompanyTree(n, amCode);
                    amTrees.add(tree1);
                } catch (Exception e) {
                    e.printStackTrace();
               }
            }
            prepStatement = conn.prepareStatement("select amb_id as id, name, parent_amb_id as parent_company_id from dom_am_best_carrier left join (select distinct ultimate_parent_amb_id as new_id from dom_am_best_carrier) as new_table on dom_am_best_carrier.amb_id= new_id where new_table.new_id is null and ultimate_parent_amb_id is null ;");

             rs = prepStatement.executeQuery();

            while(rs.next()){
                long id = rs.getLong("id");
                String name = rs.getString("name");
                long parentId = rs.getLong("parent_company_id");

                TreeNode temp = new TreeNode(id, name, parentId);
                CompanyTree tempTree = new CompanyTree(new ArrayList<>(Collections.singletonList(temp)), id);

                amTrees.add(tempTree);
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("I'm and Error");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.out.println("------");
        }

//        for (CompanyTree t : rmTrees) {
//            System.out.println();
//            t.printRecursive();
//        }

//        for (CompanyTree t: amTrees){
//            System.out.println();
//            t.printRecursive();
//
//        }

        Scanner in = new Scanner(System.in);
        System.out.println("Entering fuzzy search mode. enter an integer");
        while(in.nextInt() != -1){

            System.out.println("Fuzzy Search");
            in.nextLine();
            ArrayList<TreeNode> options = fuzzySearch(in.nextLine(), rmTrees);

            System.out.println("Here are your top " + options.size() + " results from your search:\n");

            for(int i = 0; i < options.size(); i ++){
                System.out.println((i + 1) + ": " + options.get(i));
            }
            System.out.println("\nEnter the number you would like to grab the trees for");

            int choice = in.nextInt();
            if(choice > 0 && choice  <= options.size()){

                System.out.println("Getting Related Trees");
                ArrayList<ArrayList<CompanyTree>> allRelatedTrees = options.get(choice - 1).betterGetRelatedTrees(rmTrees, amTrees);
                //options.get(choice - 1).getTree().printRecursive();
                System.out.println("Number of Related RM Trees: " + allRelatedTrees.get(0).size());
                System.out.println("Number of Related AM Trees: " + allRelatedTrees.get(1).size() + "\n");

                //System.out.println("AM Size: " + allRelatedTrees.get(0).get(0).getSize());

//                for (CompanyTree amTree : allRelatedTrees.get(1)){
//                    System.out.println(amTree.getSize());
//                }

                System.out.println("Risk Match Trees:\n");
                for (CompanyTree rmTree : allRelatedTrees.get(0)){
                    System.out.println(rmTree.toString());
                }
                System.out.println("AM Best Trees:\n");
                for (CompanyTree amTree : allRelatedTrees.get(1)){
                    System.out.println(amTree.toString());
                }
                System.out.println("Checking Similarity");
                System.out.println("Similarity " + CompanyTree.Similarity(allRelatedTrees.get(0), allRelatedTrees.get(1)));
//
//               System.out.println("Similar Nodes: ");
//                CompanyTree.similarNodes(allRelatedTrees.get(0), allRelatedTrees.get(1) );
//
                System.out.println("Unique Nodes: ");
                CompanyTree.uniqueNodes(allRelatedTrees.get(0), allRelatedTrees.get(1));
            }
        }


        //find unique nodes for the two sets of trees
        //System.out.println();
        //uniqueNodes(rmTrees, amTrees);

        //System.out.println("Starting Similarity run");


        //System.out.println("Similarity: " + CompanyTree.Similarity(rmTrees, amTrees));





        //System.out.println(rmTrees.size());
        //System.out.println(amTrees.size());
        //System.out.println("Similar Names: " + similarNodes(rmTrees,amTrees));
        //System.out.println("Size of similar Nodes: " + similarNodes(rmTrees,amTrees).get(0).size());
        System.exit(0);
    }
}
class FuzzyResult{
    private int result;
    private TreeNode node;

    FuzzyResult(TreeNode node, int result){
        this.result = result;
        this.node = node;
    }

    int getResult() {
        return result;
    }
    TreeNode getNode(){
        return node;
    }
}