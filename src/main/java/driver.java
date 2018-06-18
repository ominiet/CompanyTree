import java.io.IOException;
import java.util.ArrayList;

public class driver {

    public static void main(String [] args){
        TreeNode a = new TreeNode();
        TreeNode b = new TreeNode();
        TreeNode c = new TreeNode();
        TreeNode d = new TreeNode();

        CompanyTree ex= new CompanyTree(a);

        b.setName("alex");
        b.setId(2222);
        a.setName("Chris");
        a.setId(1111);
        c.setName("nick");
        c.setId(3333);
        a.getChildren().add(b);
        b.setParentId(1111);
        b.getChildren().add(c);
        c.setParentId(2222);


        d.setName("chad");
        d.setParentId(3333);
        d.setId(4444);

        ex.addNode(d);
       // ex.addNode(a);
        ex.printTree();

        CompanyReader reader;
        try {
            ArrayList<TreeNode> myTree;
            reader = new CompanyReader("./CSV/onebeacon_ambest.csv",true);
            myTree = reader.getCompanies();

            for (TreeNode q : myTree
                    ) {
                System.out.println(q.getId() + "    " + q.getName() + "  " + q.getParentId());

            }

          CompanyTree tree= new CompanyTree();

            //This is to get the first Root TreeNode
            for (int i =0; i< myTree.size();i++) {

                //tree.addNode(n);
                TreeNode cur = myTree.get(i);
                //    System.out.println(cur.getName());
                if (cur.getId() == cur.getParentId()) {
                    tree = new CompanyTree(cur);
                    System.out.println("HERE");
                    System.out.println(cur.getId() + "    " + cur.getName() + "  " + cur.getParentId());


                }

            }

            for (TreeNode here : myTree){

                    if(here!=tree.root)
                    tree.addNode(here);
            }

            tree.printTree();

        } catch (IOException e){
            e.printStackTrace();
        }

     System.exit(0);
    }
}
