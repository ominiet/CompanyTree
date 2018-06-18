public class driver {

    public static void main(String [] args){
        Node a = new Node();
        Node b = new Node();
        Node c = new Node();
        Node d = new Node();

        CompanyTree ex= new CompanyTree(a);
        b.companyName = "alex";
        b.id=2222;
        a.companyName="Chris";
        a.id=1111;
        c.companyName="nick";
        c.id=3333;
        a.children.add(b);
        b.parentId=1111;
        b.children.add(c);
        c.parentId=2222;

        d.companyName="chad";
        d.parentId=1111;
        d.id=4444;

        ex.addNode(d);
        ex.printTree();
    }
}
