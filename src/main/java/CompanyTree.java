import java.util.*;


public class CompanyTree {

    private TreeNode root;
    private ArrayList<TreeNode> unsorted;
    private ArrayList<TreeNode> nodeList;
    private ArrayList<String> companyNames;


    CompanyTree(TreeNode start) {
        this.root = start;
        this.unsorted = new ArrayList<>();
        this.companyNames = new ArrayList<>();
    }

    int getSize() {
        return this.companyNames.size();
    }

    ArrayList<String> getCompanyNames() {
        return companyNames;
    }

    TreeNode getRoot() {
        return root;
    }

    ArrayList<TreeNode> getNodes() {
        return nodeList;
    }


    void printRecursive() {

        String result1="";

        System.out.println(printRecursive(root, 0, result1));


    }
    void BuildTree(ArrayList<TreeNode> nodeList) {
        //System.out.println(nodeList.size());
        boolean placed;
        //go through list of nodes passed in (We already know the root)
        for (TreeNode childNode : nodeList) {
            placed = false;
            //search for a parent in the list
            for (TreeNode parentNode : nodeList) {
                //if the node is the root, add to company names and do nothing else
                if (childNode == getRoot()) {
                    this.companyNames.add(childNode.getName());
                    break;
                }
                //if the child finds a parent with a different id than its own, add to list of children
                if (childNode.getParentId() != childNode.getId()) {
                    if (childNode.getParentId() == parentNode.getId()) {
                        parentNode.getChildren().add(childNode);
                        this.companyNames.add(childNode.getName());
                        childNode.setParent(parentNode);
                        placed = true;

                    }
                } else {
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                if (childNode != root) {
                    getRoot().getChildren().add(childNode);
                }
            }
            //System.out.println();
        }
        this.nodeList = nodeList;
        unsorted.sort(new NameComparator());
        Collections.sort(this.getCompanyNames());

    }


    void reorderChildren() {
        reorderChildren(this.root);

    }

    private void reorderChildren(TreeNode node) {
        node.getChildren().sort(new NameComparator());
        for (TreeNode child : node.getChildren()) {

            reorderChildren(child);
        }

    }

    private String printRecursive(TreeNode node, int depth, String output) {
        int temp = depth;

            while (temp > 0) {

                if (temp == 1) {
                    output+="|-";
                }
                else {
                    output+="|";
                }

                output+="\t";
                temp--;
            }
            output+=node.getName();
            if(node.getRole() != null){
                output+= " - " + node.getRole();
            }
            output+="\n";
            depth++;
        for (TreeNode child : node.getChildren()) {

            output = printRecursive(child, depth,output);
        }
        return output;
    }

}

//Allows children and unsorted ArrayLists to be ordered lexically
class NameComparator implements Comparator<TreeNode> {

    public int compare(TreeNode o1, TreeNode o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}






