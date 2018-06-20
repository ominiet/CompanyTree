import java.util.*;


public class CompanyTree {
  
    private TreeNode root;

    private ArrayList<TreeNode> unsorted;
    private ArrayList<String> companyNames;

    CompanyTree(){
        this.root=null;
        this.unsorted = new ArrayList<>();
    }

    CompanyTree(TreeNode start){
        this.root=start;
        this.unsorted = new ArrayList<>();
        this.companyNames = new ArrayList<>();
    }

    public int getSize(){
        return this.companyNames.size();
    }
    public ArrayList<String> getCompanyNames() {
        return companyNames;
    }

    void BuildTree(ArrayList<TreeNode> nodeList){
        boolean placed;

        for (TreeNode childNode : nodeList){
            placed = false;
            for (TreeNode parentNode : nodeList){
                if (childNode == getRoot()) {this.companyNames.add(childNode.getName()); break;}
                if(childNode.getParentId() != childNode.getId()) {
                    if (childNode.getParentId() == parentNode.getId()) {
                        parentNode.getChildren().add(childNode);
                        this.companyNames.add(childNode.getName());
                        placed = true;

                    }
                }
                else {
                    placed = true;
                    break;
                }
            }
            if (!placed){
            //   System.out.println(root.getParentId());
            //   System.out.println(childNode.getName());

                if( childNode.getParentId() != 0 && childNode!= root){
                    getRoot().getChildren().add(childNode);
                }

                //else unsorted.add(childNode);
            }
        }
        Collections.sort(unsorted, new NameComparator());
        Collections.sort(this.getCompanyNames());
    }
    TreeNode getRoot(){
        return root;
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

        unsorted.add(find);
        return false;
    }
    public void printRecursive(){
        printRecursive(root, 0);

        if(unsorted.size() > 0) {
            System.out.println("\nUnsorted Nodes:");
            for (TreeNode node : unsorted) {
                System.out.println(node.getName());
            }
        }
    }

    private void printRecursive(TreeNode node, int depth){
        int temp = depth;
        //System.out.println(depth);
        while(temp > 0){

            if(temp == 1) System.out.print("|-");
            else System.out.print("|");

            System.out.print("\t");
            temp --;
        }
        System.out.println(node.getName());
        depth ++;
        for(TreeNode child : node.getChildren()){

            printRecursive(child, depth);
        }
    }
    void reorderChildren(){
        reorderChildren(this.root);

    }
    private void reorderChildren(TreeNode node){
        Collections.sort(node.getChildren(), new NameComparator());
        for(TreeNode child : node.getChildren()){

            reorderChildren(child);
        }

    }

}

//Allows children and unsorted ArrayLists to be ordered lexically
class NameComparator implements Comparator<TreeNode> {

    public int compare(TreeNode o1, TreeNode o2){
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}






