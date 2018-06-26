import java.io.*;
import java.sql.*;
import java.util.*;


public class driver {

    private static int similarities(CompanyTree rm, CompanyTree ambest) {

        Queue<TreeNode> queue = new LinkedList<>();

        //initialize queue with root of RiskMatch tree
        queue.add(rm.getRoot());

        //count of similar nodes in the tree
        int D = 0;

        //Traverse rm from root
        while (!queue.isEmpty()) {
            //take first item in the queue
            TreeNode next = queue.remove();

            //subtree?
            TreeNode subtree = null;

            //get the whole tree from am best to search through
            Queue<TreeNode> queue2 = new LinkedList<>();
            queue2.add(ambest.getRoot());

            //Traverse rm from root
            while (!queue2.isEmpty()) {
                //take first item in the queue
                TreeNode next2 = queue2.remove();
                if (next.compareTo(next2) == null){
                    for (TreeNode node : next.getChildren()){
                        queue.add(node);
                    }
                }
                else {
                    D += compareSubtrees(next, next2);
                }
            }

        }

        return D;
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

    static ArrayList<ArrayList<TreeNode>> similarNodes(ArrayList<CompanyTree> rm, ArrayList<CompanyTree> ambest) {
        ArrayList<TreeNode> similarInRMTree = new ArrayList<>();
        ArrayList<TreeNode> similarInAMTree = new ArrayList<>();

        //TODO: this currently has m*n*o*p time complexity. See if there is another workaround
        int x = 0;
        for (CompanyTree rmTree : rm) {
            //System.out.println("Checking Risk Match Tree (" + x++ + "/" + rm.size() + ")");
            for (CompanyTree amTree : ambest) {
                //System.out.println("Nodes from tree with " +  amTree.getRoot().getName() + " as the root");
                for (TreeNode amNode : amTree.getNodes()) {
                    for (TreeNode rmNode : rmTree.getNodes()) {
                        if (amNode.getName().equals(rmNode.getName())) {
                            similarInRMTree.add(rmNode);
                            similarInAMTree.add(amNode);
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<TreeNode>> results = new ArrayList<>();
        results.add(similarInRMTree);
        results.add(similarInAMTree);
        System.out.println("These are the names of the companies found in both trees and their parents\n");
        for (int i = 0; i < similarInRMTree.size(); i++) {
            System.out.println((i + 1) + ": " + similarInRMTree.get(i).getName() + "\n\tParent in Risk Match Tree: " +
                    similarInRMTree.get(i).getParentName() + "\n\tParent in AMBest Tree: " + similarInAMTree.get(i).getParentName() + "\n");
        }
        return results;
    }

    static ArrayList<List<String>> uniqueNodes(ArrayList<CompanyTree> rm, ArrayList<CompanyTree> ambest) {

        //Make collections and lists out of all the trees
        Collection<String> fList = new ArrayList<>();
        List<String> source = new ArrayList<>();
        for (CompanyTree rmTree : rm) {
            fList.addAll(rmTree.getCompanyNames());
            source.addAll(rmTree.getCompanyNames());
        }
        Collection<String> sList = new ArrayList<>();
        List<String> destination = new ArrayList<>();
        for (CompanyTree amTree : ambest) {
            sList.addAll(amTree.getCompanyNames());
            destination.addAll(amTree.getCompanyNames());
        }

        //remove every thing that occurs in one list from the other
        source.removeAll(sList);
        destination.removeAll(fList);

        //Print out in formatted way


        String format = "%1$-100s%2$-100s\n";
        System.out.format(format, "Only in Risk Match Tree", "Only in AM Best Tree");
        System.out.format(format, "-----------------------", "--------------------");

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("./unique"));
            //writer.write(String.format("%1$-100s%2$-100s\n",' ', (i + ": " + two)));


        for (int i = 0; i < source.size() || i < destination.size(); i++) {
            //System.out.println(i);


            if (i >= source.size() && destination.size() > i) {
                String two = destination.get(i);
                System.out.format(format, ' ', (i + ": " + two));
                writer.write(String.format("%1$-100s%2$-100s\n",' ', (i + ": " + two)));
            } else if (i < source.size() && i >= destination.size()) {
                String one = source.get(i);
                System.out.format(format, (i + ": " + one), ' ');
                writer.write(String.format("%1$-100s%2$-100s\n", (i + ": " + one), ' '));
            } else if(i < source.size() && i < destination.size()){
                System.out.format(format, (i + ": " + source.get(i)), (i + ": " + destination.get(i)));
                writer.write(String.format("%1$-100s%2$-100s\n",(i + ": " + source.get(i)), (i + ": " + destination.get(i))));
            }
        }
        writer.close();
        } catch (java.io.IOException e){
            e.printStackTrace();
        }

        ArrayList<List<String>> result = new ArrayList<>();
        result.add(source);
        result.add(destination);

        //System.out.format(format,source.size(),destination.size());
        System.out.println(destination.size());
        System.out.println(source.size());
        return result;

    }

    static int compareSubtrees(TreeNode rm, TreeNode am) {
        int D = 0;
        if (rm.getName().equals(am.getName())) D++;

        ArrayList<TreeNode> rmChildren = rm.getChildren();
        ArrayList<TreeNode> amChildren = am.getChildren();
        if (rm.getChildren().isEmpty() || am.getChildren().isEmpty()) {  //dont bother checking further if there arent any children
            return D;
        }
        for (TreeNode node : rmChildren) {
            for (TreeNode amnode : amChildren) {
                if (node.getName().equals(amnode.getName())) {
                    D += compareSubtrees(node, amnode);
                }
            }

        }
        return D;
    }

    static double Similarity(ArrayList<CompanyTree> rm, ArrayList<CompanyTree> ambest) {
        double sim = 0;
        double N = 0;
        for (CompanyTree rmTree : rm) {
            N += rmTree.getSize();
        }
        for (CompanyTree amTree : ambest) {
            N += amTree.getSize();
        }
        for (CompanyTree rmTree : rm) {
            for (CompanyTree amTree : ambest) {
                //double N = rmTree.getSize();
                //N += amTree.getSize();
                double D = similarities(rmTree, amTree);
                 System.out.println("Nodes: " + N+" D: "+D);
                if((D / (N - D)) > 0) {
                    System.out.println(N);
                    System.out.println(D);
                    System.out.println("individual Sim Result: " + (D / (N - D)));
                }
                sim += (D / (N - D));

            }
        }
        return sim;
    }

    static ArrayList<ArrayList<Long>> getAllTopLevelCompanies(Properties prop) {
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

    public static void main(String[] args) {

        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        ArrayList<TreeNode> nodes2 = new ArrayList<TreeNode>();

        ArrayList<CompanyTree> rmTrees = new ArrayList<>();
        ArrayList<CompanyTree> amTrees = new ArrayList<>();
        Connection conn = null;

        CompanyTree tree = null;
        CompanyTree tree2 = null;
        CompanyTree tree3 = null;

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


        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println();
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
////
//        for (CompanyTree t: amTrees){
//            System.out.println();
//            t.printRecursive();
//
//        }



        //find unique nodes for the two sets of trees
        //System.out.println();
        //uniqueNodes(rmTrees, amTrees);


        System.out.println();
        System.out.println("Similarity: " + Similarity(rmTrees, amTrees));

        System.out.println();



        //System.out.println(rmTrees.size());
        //System.out.println(amTrees.size());
        //System.out.println("Similar Names: " + similarNodes(rmTrees,amTrees));
        //System.out.println("Size of similar Nodes: " + similarNodes(rmTrees,amTrees).get(0).size());
        System.exit(0);
    }
}
