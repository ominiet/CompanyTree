import java.io.IOException;
import java.util.ArrayList;

public class driver {

    public static void main(String [] args){
        CompanyReader reader;
        try {
            ArrayList<TreeNode> myTree;
            reader = new CompanyReader("./CSV/onebeacon_ambest.csv",true);
            myTree = reader.getCompanies();

            CompanyTree tree = new CompanyTree();

            //This is to get the first Root TreeNode
            for (int i =0; i< myTree.size();i++) {

                TreeNode cur = myTree.get(i);
                if(cur.getId() == reader.getUltimateParent())
                    tree = new CompanyTree(cur);
            }

            tree.BuildTree(myTree);

//            for (TreeNode currentNode : myTree){
//                    if(currentNode!=tree.getRoot())
//                    tree.addNode(currentNode);
//            }

            tree.printRecursive();

        } catch (IOException e){
            e.printStackTrace();
        }

     System.exit(0);
    }
}
