import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.text.similarity.FuzzyScore;


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

    private static CompanyTree populateFromDatabase(Statement statement, String query) {

        int top_level;
        ArrayList<TreeNode> nodes = new ArrayList<>();
        CompanyTree tree1 = null;

        try {
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                int parentId = result.getInt("parent_company_id");
                top_level = result.getInt("top_level_company_id");

                if (top_level == 0) {
                    top_level = id;
                }
                TreeNode temp = new TreeNode(id, name, parentId);
                nodes.add(temp);

                if (top_level == id) {
                    tree1 = new CompanyTree(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tree1.BuildTree(nodes);
            tree1.reorderChildren();
        } catch (NullPointerException e) {
            System.out.println("Error Building Tree");
        }

        return tree1;
    }


    ///Search for an exact match of name string your set of risk match trees
    private static void fuzzySearch(String search, ArrayList<CompanyTree> rm){

        FuzzyScore score = new FuzzyScore(Locale.ENGLISH);

        int mark = (int)((double)score.fuzzyScore(search, search) * .30);
        System.out.println("Mark: " + mark);

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
        System.out.println();
        System.out.println(matches.size());
        for (FuzzyResult n : matches) {
            System.out.println(n.getNode() + " " + n.getResult());
        }
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


            PreparedStatement prepStatement = conn.prepareStatement("select dom_company.name, dom_company.id, parent_company_id, dom_company_role.name as role from dom_company left join dom_company_role on dom_company.primary_role_id = dom_company_role.id where top_level_company_id=? or dom_company.id = ?;");
            for (long rmCode : topLevelCompanyCodes.get(0)) {
                prepStatement.setString(1, Long.toString(rmCode));
                prepStatement.setString(2, Long.toString(rmCode));

                ResultSet rs = prepStatement.executeQuery();
                ArrayList<TreeNode> n = new ArrayList<>();
                CompanyTree tree1 = null;

                try {

                    while (rs.next()) {
                        long id = rs.getLong("id");
                        String name = rs.getString("name");
                        long parentId = rs.getLong("parent_company_id");
                        String role = rs.getString("role");


                        if (rmCode == 0) {
                            rmCode = id;
                        }
                        TreeNode temp = new TreeNode(id, name, parentId,role);
                        n.add(temp);

                        if (rmCode == id) {
                            tree1 = new CompanyTree(temp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tree1.BuildTree(n);
                    tree1.reorderChildren();

                    rmTrees.add(tree1);
                } catch (NullPointerException e) {
                    System.out.println("Error Building Tree");
                }

            }
            prepStatement = conn.prepareStatement("select dom_company.id, dom_company.name,dom_company.parent_company_id, dom_company_role.name as role from dom_company left join dom_company_role on dom_company.primary_role_id = dom_company_role.id left join (select distinct top_level_company_id as new_id from dom_company) as new_table on dom_company.id = new_id where new_table.new_id is null and dom_company.top_level_company_id is null;");

            ResultSet rs = prepStatement.executeQuery();

            while(rs.next()){
                long id = rs.getLong("id");
                String name = rs.getString("name");
                long parentId = rs.getLong("parent_company_id");
                String role = rs.getString("role");

                TreeNode temp = new TreeNode(id, name, parentId,role);
                CompanyTree tempTree = new CompanyTree(temp);
                tempTree.BuildTree(new ArrayList<>(Arrays.asList(temp)));

                rmTrees.add(tempTree);

            }



            prepStatement = conn.prepareStatement("select amb_id as id, name, parent_amb_id as parent_company_id, ultimate_parent_amb_id as top_level_company_id from dom_am_best_carrier where ultimate_parent_amb_id = ? OR amb_id = ?;");
            for (long amCode : topLevelCompanyCodes.get(1)) {
                prepStatement.setString(1, Long.toString(amCode));
                prepStatement.setString(2, Long.toString(amCode));

                rs = prepStatement.executeQuery();
                ArrayList<TreeNode> n = new ArrayList<>();
                CompanyTree tree1 = null;

                try {

                    while (rs.next()) {

                        long id = rs.getLong("id");
                        String name = rs.getString("name");
                        long parentId = rs.getLong("parent_company_id");


                        if (amCode == 0) {
                            amCode = id;
                        }
                        TreeNode temp = new TreeNode(id, name, parentId);
                        n.add(temp);

                        if (amCode == id) {
                            tree1 = new CompanyTree(temp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(tree1 == null){
                        TreeNode temp = new TreeNode(amCode, ("id: " + amCode), 0);
                        tree1 = new CompanyTree(temp);
                        tree1.BuildTree(new ArrayList<>(Arrays.asList(temp)));
                    }

                    tree1.BuildTree(n);
                    tree1.reorderChildren();

                    amTrees.add(tree1);
                } catch (NullPointerException e) {
                    System.out.println("Error Building Tree");
                    System.out.println(n.get(0).getName());
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
                CompanyTree tempTree = new CompanyTree(temp);
                tempTree.BuildTree(new ArrayList<>(Arrays.asList(temp)));

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

        //fuzzySearch("indiana insurance company", rmTrees);
        Scanner in = new Scanner(System.in);
        while(in.nextInt() != -1){


            System.out.println("Fuzzy Search");
            in.nextLine();
            fuzzySearch(in.nextLine(), rmTrees);
        }


        //find unique nodes for the two sets of trees
        //System.out.println();
        //uniqueNodes(rmTrees, amTrees);

        //System.out.println("Starting Similarity run");
        System.out.println();

        //System.out.println("Similarity: " + CompanyTree.Similarity(rmTrees, amTrees));

        System.out.println();



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

    public int getResult() {
        return result;
    }
    public TreeNode getNode(){
        return node;
    }
}

