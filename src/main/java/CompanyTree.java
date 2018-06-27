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

    CompanyTree(ArrayList<TreeNode> nodes, long rootCode) {
        this.unsorted = new ArrayList<>();
        this.companyNames = new ArrayList<>();
        //find root in list of nodes
        boolean setRootFlag = false;
        for (TreeNode node : nodes) {
            if (rootCode == node.getId()) {
                this.root = node;
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

    @Override
    public String toString() {
        return printRecursive(root, 0, "");
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

     public static ArrayList<ArrayList<TreeNode>> similarNodes(ArrayList<CompanyTree> rm, ArrayList<CompanyTree> ambest) {
        ArrayList<TreeNode> similarInRMTree = new ArrayList<>();
        ArrayList<TreeNode> similarInAMTree = new ArrayList<>();

        //TODO: this currently has m*n*o*p time complexity. See if there is another workaround
        int x = 0;
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
        System.out.println("These are the names of the companies found in both trees and their parents\n");
        for (int i = 0; i < similarInRMTree.size(); i++) {
            System.out.println((i + 1) + ": " + similarInRMTree.get(i).getName() + "\n\tParent in Risk Match Tree: " +
                    similarInRMTree.get(i).getParentName() + "\n\tParent in AMBest Tree: " + similarInAMTree.get(i).getParentName() + "\n");
        }
        return results;
    }

    public static ArrayList<List<String>> uniqueNodes(ArrayList<CompanyTree> rm, ArrayList<CompanyTree> ambest) {

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

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("./unique"));
            //writer.write(String.format("%1$-100s%2$-100s\n",' ', (i + ": " + two)));


            for (int i = 0; i < source.size() || i < destination.size(); i++) {
                //System.out.println(i);


                if (i >= source.size() && destination.size() > i) {
                    String two = destination.get(i);
                    System.out.format(format, ' ', (i + ": " + two));
                    writer.write(String.format("%1$-100s%2$-100s\n",' ', (i + ": " + two)));
                } else if (i < source.size() && i >= destination.size()) {
                    String one = source.get(i);
                    System.out.format(format, (i + ": " + one), ' ');
                    writer.write(String.format("%1$-100s%2$-100s\n", (i + ": " + one), ' '));
                } else if(i < source.size() && i < destination.size()){
                    System.out.format(format, (i + ": " + source.get(i)), (i + ": " + destination.get(i)));
                    writer.write(String.format("%1$-100s%2$-100s\n",(i + ": " + source.get(i)), (i + ": " + destination.get(i))));
                }
            }
            writer.close();
        } catch (java.io.IOException e){
            e.printStackTrace();
        }

        ArrayList<List<String>> result = new ArrayList<>();
        result.add(source);
        result.add(destination);

        //System.out.format(format,source.size(),destination.size());
        System.out.println(destination.size());
        System.out.println(source.size());
        return result;

    }

    public static double Similarity(ArrayList<CompanyTree> rm, ArrayList<CompanyTree> ambest) {
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
            System.out.print("(" + (i++ + 1) + "/" + rm.size() + ") Checking similarities for tree: " +  rmTree.getRoot().getName());
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
                if (next.compareTo(next2) != null){

                    isFoundFlag = true;
                    D += compareSubtrees(next, next2);
                    queue.clear();
                    break;
                }

                for (TreeNode child : next2.getChildren()){
                    queue2.add(child);
                }
            }
            if(!isFoundFlag){
                for (TreeNode node : next.getChildren()){
                    queue.add(node);
                }
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






