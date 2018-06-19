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
        Connection conn = null;

        CompanyTree tree = null;

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
        System.out.println("line");
        tree.printRecursive();


        System.exit(0);
    }
}
