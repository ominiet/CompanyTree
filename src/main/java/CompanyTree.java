import java.util.ArrayList;

class Node{
    String companyName;
    int id;
    ArrayList<Node> children;

    Node(){
        this.companyName="unset";
        this.id=0000;
        this.children= new ArrayList<Node>();
    }
}
public class CompanyTree {

    void printTree(Node root){

        for(Node a: root.children){
            
        }

    }

public static void main(String [] args){
    Node a = new Node();
    Node b = new Node();

    b.companyName = "alex";
    a.companyName="Chris";
    a.children.add(b);


}

}
