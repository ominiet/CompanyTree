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


/*    boolean addNode(TreeNode find) {

        Queue<TreeNode> queue = new LinkedList<TreeNode>();

        queue.add(root);
        while (true) {

            int nodeCount = queue.size();
            if (nodeCount == 0) {
                break;
            }

            while (nodeCount > 0) {
                TreeNode node = queue.peek();

                if (node.getId() == find.getParentId()) {
                    node.getChildren().add(find);
                    return true;
                }
                if (node.getId() != find.getId())
                    queue.remove();
                for (TreeNode child : node.getChildren()) {
                    queue.add(child);
                }
                nodeCount--;
            }
            // System.out.println();
        }

        unsorted.add(find);
        return false;
    }*/

    void printRecursive() {
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
        //System.out.println(depth);
        while (temp > 0) {

            if (temp == 1) System.out.print("|-");
            else System.out.print("|");

            System.out.print("\t");
            temp--;
        }
        System.out.println(node.getName());
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






