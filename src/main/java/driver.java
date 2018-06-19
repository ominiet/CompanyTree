import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;


public class driver {

    static CompanyTree  populateFromDatabase(Statement statement, String query){

        int top_level = 0;
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        CompanyTree tree1 = null;

        try{
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                int parentId = result.getInt("parent_company_id");
                top_level = result.getInt("top_level_company_id");

                if ( top_level == 0){
                    top_level=id;
                }
                TreeNode temp = new TreeNode(id, name, parentId);
                nodes.add(temp);

                if (top_level == id) {
                    tree1 = new CompanyTree(temp);
                }

            }


        }
        catch(Exception e ){
            e.printStackTrace();
        }
        tree1.BuildTree(nodes);
        return tree1;
    }

    static ArrayList<List<String>> uniqueNodes(CompanyTree first, CompanyTree second){


        Collection<String> fList = first.getCompanyNames();
        Collection<String> sList = second.getCompanyNames();
        List<String> source = first.getCompanyNames();
        List<String> destination = second.getCompanyNames();

        source.removeAll(sList);
        destination.removeAll(fList);

        System.out.println();



        String format = "%1$-100s%2$-100s\n";
        System.out.format(format,"First Tree","Second Tree");
        for( int i=0;i<source.size()-1 || i< destination.size()-1;i++) {



            if (i > source.size()-1 && destination.size()-1<=i) {
                String two = destination.get(i);
                System.out.format(format,' ',two);
            }
            else if (i < source.size()-1 && i >= destination.size()-1) {
                String one = source.get(i);
                System.out.format(format,one,' ');
            } else {
                System.out.format(format,source.get(i),destination.get(i));
            }
        }

        ArrayList<List<String>> result = new ArrayList<List<String>>();
        result.add(source);
        result.add(destination);

        System.out.format(format,source.size(),destination.size());

        return result;

    }
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
            String query2 = "select amb_id as id, name, parent_amb_id as parent_company_id , ultimate_parent_amb_id as top_level_company_id from dom_am_best_carrier where ultimate_parent_amb_id=51160 OR amb_id=51160;";
            String query3= "select amb_id as id, name, parent_amb_id as parent_company_id , ultimate_parent_amb_id as top_level_company_id from dom_am_best_carrier where ultimate_parent_amb_id=58167 OR amb_id=58167;";

            tree = populateFromDatabase(statement,query);
            tree2 = populateFromDatabase(statement,query2);


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


        tree.reorderChildren();
      
        tree2.reorderChildren();

        tree.printRecursive();

        System.out.println();

        tree2.printRecursive();


        uniqueNodes(tree,tree2);
        System.exit(0);
    }
}
