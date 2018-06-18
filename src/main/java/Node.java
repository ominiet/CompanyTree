import java.util.ArrayList;

public class Node {

        String companyName;
        int id;
        ArrayList<Node> children;
        int parentId;

        Node(){
            this.companyName="unset";
            this.id=0000;
            this.children= new ArrayList<Node>();
            this.parentId=0000;
    }
}
