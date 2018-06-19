import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


public class driver {

    public static void main(String[] args) {

        int top_level = 0;

        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        ArrayList<TreeNode> nodes2 = new ArrayList<TreeNode>();

        Connection conn = null;

        CompanyTree tree = null;
        CompanyTree tree2 = null;

        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("./Props/config.properties");
            prop.load(input);

            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/masterdb",
                            prop.getProperty("username"), prop.getProperty("password"));

            // Do something with the Connection
            Statement statement = conn.createStatement();
            String query = "select id,name,parent_company_id,top_level_company_id from dom_company where top_level_company_id=3089 OR id=3089;";
            ResultSet result = statement.executeQuery(query);

            //For first Tree
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                int parentId = result.getInt("parent_company_id");
                top_level = result.getInt("top_level_company_id");

                TreeNode temp = new TreeNode(id, name, parentId);
                nodes.add(temp);

                if (top_level == 0) {
                    tree = new CompanyTree(temp);
                }

            }


            String query2 = "select amb_id as id, name, parent_amb_id as parent_company_id , ultimate_parent_amb_id as top_level_company_id from dom_am_best_carrier where ultimate_parent_amb_id=51160 OR amb_id=51160;";
            ResultSet result2 = statement.executeQuery(query2);

            while (result2.next()) {
                int id = result2.getInt("id");
                String name = result2.getString("name");
                int parentId = result2.getInt("parent_company_id");
                top_level = result2.getInt("top_level_company_id");


                TreeNode temp = new TreeNode(id, name, parentId);
                nodes2.add(temp);

                if (top_level == id) {
                    tree2 = new CompanyTree(temp);
                }

            }

        } catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println();
            e.printStackTrace();
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }


        tree.BuildTree(nodes);
        tree.reorderChildren();
      
        tree2.BuildTree(nodes2);
        tree2.reorderChildren();

        tree.printRecursive();

        System.out.println();

        tree2.printRecursive();

        System.exit(0);
    }
}
