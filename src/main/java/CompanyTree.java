import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class CompanyTree {
    //colors for changing output
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    private TreeNode root;
    private ArrayList<TreeNode> nodeList;
    private ArrayList<String> companyNames;

    /**
     * @param start A complete Tree Node representative of the root of the Tree
     */
    CompanyTree(TreeNode start) {
        this.root = start;

        this.companyNames = new ArrayList<>();
    }

    /**
     * @param nodes    The set of Tree Nodes in the tree
     * @param rootCode The code of the top level company. This determines the root of the tree
     */
    CompanyTree(ArrayList<TreeNode> nodes, long rootCode) {

        this.companyNames = new ArrayList<>();
        //find root in list of nodes
        boolean setRootFlag = false;
        for (TreeNode node : nodes) {
            if (rootCode == node.getId()) {
                this.root = node;
                this.root.setTree(this);
                setRootFlag = true;
                break;
            }
        }
        //create
        if (!setRootFlag) {
            this.root = new TreeNode(rootCode, Long.toString(rootCode), 0);
        }

        //perform all other operations we were doing before
        BuildTree(nodes);
    }

    @Override
    public String toString() {
        return printRecursive(root, 0, "");
    }

    private int getSize() {
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

    void BuildTree(ArrayList<TreeNode> nodeList) {
        //System.out.println(nodeList.size());
        boolean placed;
        //go through list of nodes passed in (We already know the root)
        for (TreeNode childNode : nodeList) {
            placed = false;
            //search for a parent in the list
            for (TreeNode parentNode : nodeList) {
                childNode.setTree(this);
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
                        //childNode.setTree(this);
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
        Collections.sort(this.getCompanyNames());
        reorderChildren();

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

    void printRecursive() {

        String result1 = "";

        System.out.println(printRecursive(root, 0, result1));


    }

    private String printRecursive(TreeNode node, int depth, String output) {
        int temp = depth;

        StringBuilder outputBuilder = new StringBuilder(output);
        while (temp > 0) {

            if (temp == 1) {
                outputBuilder.append("|-");
            } else {
                outputBuilder.append("|");
            }

            outputBuilder.append("\t");
            temp--;
        }
        output = outputBuilder.toString();
        output += node.getName();
        if (node.getRole() != null) {
            output += " - " + node.getRole();
        }
        output += "\n";
        depth++;
        for (TreeNode child : node.getChildren()) {

            output = printRecursive(child, depth, output);
        }
        return output;
    }

    /**
     * @param rm     Array list of Company Trees built from Risk Match Table
     * @param ambest Array list of Company Trees built from AM Best Table
     *
     * @return Array List of nodes that appear in both lists of trees
     */
    static ArrayList<ArrayList<TreeNode>> similarNodes(ArrayList<CompanyTree> rm, ArrayList<CompanyTree> ambest) {
        ArrayList<TreeNode> similarInRMTree = new ArrayList<>();
        ArrayList<TreeNode> similarInAMTree = new ArrayList<>();

        for (CompanyTree rmTree : rm) {
            //System.out.println("Checking Risk Match Tree (" + x++ + "/" + rm.size() + ")");
            for (CompanyTree amTree : ambest) {
                //System.out.println("Nodes from tree with " +  amTree.getRoot().getName() + " as the root");
                for (TreeNode amNode : amTree.getNodes()) {
                    for (TreeNode rmNode : rmTree.getNodes()) {
                        if (amNode.getName().equals(rmNode.getName())) {
                            similarInRMTree.add(rmNode);
                            similarInAMTree.add(amNode);
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<TreeNode>> results = new ArrayList<>();
        results.add(similarInRMTree);
        results.add(similarInAMTree);

        return results;
    }
    static void printSimilarNodes(ArrayList<ArrayList<TreeNode>> results) {
        System.out.println("These are the names of the companies found in both trees and their parents\n");

        for (int i = 0; i < results.get(0).size(); i++) {
            if(results.get(0).get(i).getParentName().equals(results.get(1).get(i).getParentName()))
                System.out.print(GREEN);
            else System.out.print(RED);
            System.out.println((i + 1) + ": " + results.get(0).get(i).getName() + "\n\tParent in Risk Match Tree: " +
                    results.get(0).get(i).getParentName() + "\n\tParent in AMBest Tree: " + results.get(1).get(i).getParentName() + "\n");
        }
        System.out.println(RESET);
    }

    static ArrayList<List<String>> uniqueNodes(ArrayList<CompanyTree> rm, ArrayList<CompanyTree> ambest) {

        //Make collections and lists out of all the trees
        Collection<String> fList = new ArrayList<>();
        List<String> source = new ArrayList<>();
        for (CompanyTree rmTree : rm) {
            fList.addAll(rmTree.getCompanyNames());
            source.addAll(rmTree.getCompanyNames());
        }
        Collection<String> sList = new ArrayList<>();
        List<String> destination = new ArrayList<>();
        for (CompanyTree amTree : ambest) {
            sList.addAll(amTree.getCompanyNames());
            destination.addAll(amTree.getCompanyNames());
        }

        //remove every thing that occurs in one list from the other
        source.removeAll(sList);
        destination.removeAll(fList);

        //Print out in formatted way


        String format = "%1$-100s%2$-100s\n";
        System.out.format(format, "Only in Risk Match Tree", "Only in AM Best Tree");
        System.out.format(format, "-----------------------", "--------------------");
        for (int i = 0; i < source.size() || i < destination.size(); i++) {

            if (i >= source.size() && destination.size() > i) {
                String two = destination.get(i);
                System.out.format(format, ' ', ((i + 1) + ": " + two));
            } else if (i < source.size() && i >= destination.size()) {
                String one = source.get(i);
                System.out.format(format, ((i + 1) + ": " + one), ' ');
            } else if (i < source.size() && i < destination.size()) {
                System.out.format(format, ((i + 1) + ": " + source.get(i)), ((i + 1) + ": " + destination.get(i)));
            }
        }
        ArrayList<List<String>> result = new ArrayList<>();
        result.add(source);
        result.add(destination);

        return result;

    }

    static double Similarity(ArrayList<CompanyTree> rm, ArrayList<CompanyTree> ambest) {
        double sim = 0;
        double N = 0;
        for (CompanyTree rmTree : rm) {
            N += rmTree.getSize();
        }
        for (CompanyTree amTree : ambest) {
            N += amTree.getSize();
        }
        int i = 0;
        System.out.println();
        for (CompanyTree rmTree : rm) {
            System.out.print("\r");
            System.out.print("(" + (i++ + 1) + "/" + rm.size() + ") Checking similarities for tree: " + rmTree.getRoot().getName());
            for (CompanyTree amTree : ambest) {
                double D = similarities(rmTree, amTree);
                sim += (D / (N - D));
            }
        }
        System.out.print("\r");
        return sim;
    }

    private static int similarities(CompanyTree rm, CompanyTree ambest) {
        Queue<TreeNode> queue = new LinkedList<>();

        //initialize queue with root of RiskMatch tree
        queue.add(rm.getRoot());

        //count of similar nodes in the tree
        int D = 0;


        boolean isFoundFlag;
        //Traverse rm from root
        while (!queue.isEmpty()) {
            isFoundFlag = false;
            //take first item in the queue
            TreeNode next = queue.remove();

            //get the whole tree from am best to search through
            Queue<TreeNode> queue2 = new LinkedList<>();
            queue2.add(ambest.getRoot());

            //Traverse rm from root
            while (!queue2.isEmpty()) {
                //take first item in the queue
                TreeNode next2 = queue2.remove();
                if (next.compareTo(next2) != null) {

                    isFoundFlag = true;
                    D += compareSubtrees(next, next2);
                    queue.clear();
                    break;
                }
                queue2.addAll(next2.getChildren());
            }
            if (!isFoundFlag) {
                queue.addAll(next.getChildren());
            }

        }
        return D;
    }

    private static int compareSubtrees(TreeNode rm, TreeNode am) {
        int D = 0;
        if (rm.getName().equals(am.getName())) D++;

        ArrayList<TreeNode> rmChildren = rm.getChildren();
        ArrayList<TreeNode> amChildren = am.getChildren();
        if (rm.getChildren().isEmpty() || am.getChildren().isEmpty()) {  //dont bother checking further if there arent any children
            return D;
        }
        for (TreeNode node : rmChildren) {
            for (TreeNode amnode : amChildren) {
                if (node.getName().equals(amnode.getName())) {
                    D += compareSubtrees(node, amnode);
                }
            }

        }
        return D;
    }
}

//Allows children and unsorted ArrayLists to be ordered lexically
class NameComparator implements Comparator<TreeNode> {

    public int compare(TreeNode o1, TreeNode o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
