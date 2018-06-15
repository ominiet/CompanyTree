public class driver {

    public static void main(String [] args){
        Node a = new Node();
        Node b = new Node();


        CompanyTree ex= new CompanyTree(a);
        b.companyName = "alex";
        a.companyName="Chris";
        a.children.add(b);


        ex.printTree(a);
    }
}
