import java.util.ArrayList;

public class CompanyTree {

    public class Node{
        String companyName;
        int id;
        ArrayList<Integer> children;

        Node(){
          this.companyName="unset";
          this.id=0000;
          this.children= new ArrayList<Integer>();
        }
    }
}
