
import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class CompanyTree {
  
TreeNode root;


    CompanyTree(){
        this.root=null;
    }

    CompanyTree(TreeNode start){

        this.root=start;
    }


    boolean addNode(TreeNode find){

        Queue<TreeNode> queue = new LinkedList<TreeNode>();

        queue.add(root);
        while(true){

            int nodeCount = queue.size();
            if(nodeCount==0){
                break;
            }

            while(nodeCount>0){
                TreeNode node = queue.peek();
              //  System.out.print(node.getName() + " ");


                if (node.getId()==find.getParentId()){
                    node.getChildren().add(find);
                    return true;
                }
                if(node.getId()!=find.getId())
                queue.remove();
                for (TreeNode child: node.getChildren()) {
                    queue.add(child);
                }
                nodeCount--;
            }
           // System.out.println();
        }

        return false;
    }
    void printTree() {

        Queue<TreeNode> queue = new LinkedList<TreeNode>();

        queue.add(root);
        while(true){

            int nodeCount = queue.size();
            if(nodeCount==0){
                break;
            }

            while(nodeCount>0){
                TreeNode node = queue.peek();
                System.out.print(node.getName() + " ");
                queue.remove();
                for (TreeNode child: node.getChildren() ) {
                    queue.add(child);
                }
                nodeCount--;
            }
            System.out.println();
        }

    }
}
//    void printTreeStruct() {
//
//        try {
//            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
//        }
//        catch(Exception e){};
//
//        Queue<Node> queue = new LinkedList<Node>();
//        queue.add(root);
//
//        String struct=("| ");
//        while(true){
//
//
//            int nodeCount = queue.size();
//            if(nodeCount==0){
//                break;
//            }
//
//            while(nodeCount>0){
//                Node node = queue.peek();
//
//                System.out.print(node.companyName + " ");
//                queue.remove();
//                for (Node child: node.children) {
//                    queue.add(child);
//                }
//                nodeCount--;
//            }
//            System.out.println();
//        }
//
//    }





