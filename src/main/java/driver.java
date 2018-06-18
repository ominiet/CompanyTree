import java.io.IOException;
import java.util.ArrayList;

public class driver {

    public static void main(String [] args){
        TreeNode a = new TreeNode();
        TreeNode b = new TreeNode();
        TreeNode c = new TreeNode();
        TreeNode d = new TreeNode();

    //    CompanyTree ex= new CompanyTree(a);

//        b.companyName = "alex";
//        b.id=2222;
//        a.companyName="Chris";
//        a.id=1111;
//        c.companyName="nick";
//        c.id=3333;
//        a.children.add(b);
//        b.parentId=1111;
//        b.children.add(c);
//        c.parentId=2222;
//
//        d.companyName="chad";
//        d.parentId=1111;
//        d.id=4444;


        CompanyReader reader;
        try {
            ArrayList<TreeNode> myTree;
            reader = new CompanyReader("./CSV/onebeacon_ambest.csv",true);
            myTree = reader.getCompanies();

//            for (TreeNode q : myTree
//                 ) {
//                System.out.println(q.getId() + "    " + q.getName() + "  " + q.getParentId());
//
//            }
          CompanyTree tree= new CompanyTree();


                //for current
                for(int j=0; j< myTree.size()-1;j++){
                    TreeNode cur = myTree.get(j);

                    //Compare to next
                    for (int k=1;k<myTree.size()-1;k++){
                        TreeNode next = myTree.get(k);

                        //add current if its next's parent
                        if (cur.getId()==next.getParentId()){

                           // System.out.println(cur.getName() + "   " + next.getName());
                           // System.out.println(cur.getId() + "   " + next.getParentId());
                            cur.getChildren().add(next);
                        }
                    }
                }


            //This is to get the first Root TreeNode
            for (int i =0; i< myTree.size();i++) {

                //tree.addNode(n);
                TreeNode cur = myTree.get(i);
                //    System.out.println(cur.getName());
                if (cur.getId() == cur.getParentId()) {
                    tree = new CompanyTree(cur);
                    System.out.println("HERE");

                }

            }

//            for (TreeNode here : myTree){
//
//                    tree.addNode(here);
//            }

            tree.printTree();

        } catch (IOException e){
            e.printStackTrace();
        }

     System.exit(0);
    }
}
