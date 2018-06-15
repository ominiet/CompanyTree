


public class CompanyTree {

    Node root;

    CompanyTree(Node start){
        this.root=start;
    }

    void printTree(Node root) {


        for (Node a : root.children) {
            System.out.println(a.companyName);
        }

    }

}



