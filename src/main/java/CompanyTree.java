import java.io.BufferedWriter;
import java.io.FileWriter;
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

    TreeNode getRoot() {
        return root;
    }


    void printRecursive() {
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("./rmTrees.txt", true));
//            writer.write("\n");
//            writer.close();
//        } catch(java.io.IOException e ){
//           e.printStackTrace();
//        }
        printRecursive(root, 0);

        if (unsorted.size() > 0) {
            System.out.println("\nUnsorted Nodes:");
            for (TreeNode node : unsorted) {
                System.out.println(node.getName());
            }
        }
    }

    private void printRecursive(TreeNode node, int depth) {
        int temp = depth;
        //try {
            //BufferedWriter writer = new BufferedWriter(new FileWriter("./rmTrees.txt", true));
            while (temp > 0) {

                if (temp == 1) {
                    System.out.print("|-");
                    //writer.write("|-");
                }
                else {System.out.print("|");
                    //writer.write("|");
                }

                System.out.print("\t");
                //writer.write("\t");
                temp--;
            }
            System.out.print(node.getName());
            //writer.write(node.getName());
            if(node.getRole() != null){
                System.out.print(" - " + node.getRole());
                //writer.write(" - " + node.getRole());
            }
            System.out.println();
            //writer.newLine();
            //writer.close();
        //} catch (java.io.IOException e){
        //    e.printStackTrace();
        //}
        depth++;
        for (TreeNode child : node.getChildren()) {

            printRecursive(child, depth);
        }
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

    ArrayList<TreeNode> getNodes() {
        return nodeList;
    }
}

//Allows children and unsorted ArrayLists to be ordered lexically
class NameComparator implements Comparator<TreeNode> {

    public int compare(TreeNode o1, TreeNode o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}






