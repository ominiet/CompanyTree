import javax.xml.soap.Node;
import java.io.IOException;
import java.util.ArrayList;

public class CompanyTree {

    void printTree(Node root) {



    }

    public static void main(String[] args) {
        CompanyReader reader;
        try {
            ArrayList<TreeNode> myTree;
            reader = new CompanyReader("./CSV/onebeacon_ambest.csv",true);
            myTree = reader.getCompanies();
            for (TreeNode n: myTree) {
                System.out.println(n.getName());
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
