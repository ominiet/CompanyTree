import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class driver {

    public static void main(String [] args){

    int top_level=0;

        ArrayList<TreeNode> nodes=new ArrayList<TreeNode>();
        Connection conn = null;

        CompanyTree tree = new CompanyTree();
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/masterdb",
                            "#","#");

            // Do something with the Connection
            Statement statement = conn.createStatement();
            String query  = "select id,name,parent_company_id,top_level_company_id from dom_company where top_level_company_id=3089 OR id=3089;";

           ResultSet result = statement.executeQuery(query);
            while (result.next()){
                int id = result.getInt("id");
                String name = result.getString("name");
                int parentId = result.getInt("parent_company_id");
                top_level = result.getInt("top_level_company_id");

                 TreeNode temp = new TreeNode(id,name,parentId);
                nodes.add(temp);

                if (top_level == 0) {
                    tree = new CompanyTree(temp);
                }
              //  System.out.println(temp.getId() + ", " + temp.getName() +
                       // ", " + temp.getParentId());
            }

        } catch(NumberFormatException e){e.printStackTrace();}catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }





// Old functionality
//        CompanyReader reader;
//        try {
//            ArrayList<TreeNode> myTree;
//            reader = new CompanyReader("./CSV/onebeacon_ambest.csv",true);
//            myTree = reader.getCompanies();
//
//            CompanyTree tree = new CompanyTree();
//
//            //This is to get the first Root TreeNode
//            for (int i =0; i< myTree.size();i++) {
//
//                TreeNode cur = myTree.get(i);
//                if(cur.getId() == reader.getUltimateParent())
//                    tree = new CompanyTree(cur);
//            }


            tree.BuildTree(nodes);

            tree.printRecursive();


     System.exit(0);
    }
}
