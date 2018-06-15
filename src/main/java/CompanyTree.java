import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class CompanyTree {

    Node root;

    CompanyTree(Node start){

        this.root=start;
    }


    boolean addNode(Node find){

        Queue<Node> queue = new LinkedList<Node>();

        queue.add(root);
        while(true){

            int nodeCount = queue.size();
            if(nodeCount==0){
                break;
            }

            while(nodeCount>0){
                Node node = queue.peek();
                //System.out.print(node.companyName + " ");

                if (node.id==find.parentId){
                    node.children.add(find);
                    return true;
                }
                if(node.id!=find.id)
                queue.remove();
                for (Node child: node.children) {
                    queue.add(child);
                }
                nodeCount--;
            }
            System.out.println();
        }

        return false;
    }
    void printTree() {

        Queue<Node> queue = new LinkedList<Node>();

        queue.add(root);
        while(true){

            int nodeCount = queue.size();
            if(nodeCount==0){
                break;
            }

            while(nodeCount>0){
                Node node = queue.peek();
                System.out.print(node.companyName + " ");
                queue.remove();
                for (Node child: node.children) {
                    queue.add(child);
                }
                nodeCount--;
            }
            System.out.println();
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

}



