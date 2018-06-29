import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class TreeNode {
    private String companyName;
    private long id;
    private long parentId;
    private String role;
    private TreeNode parent;
    private CompanyTree tree;
    private ArrayList<TreeNode> children;

    TreeNode(long id, String name, long parentId) {
        this.companyName = name;
        this.children = new ArrayList<>();
        this.id = id;
        this.parentId = parentId;
        this.parent = null;
        this.role = null;
        this.tree = null;
    }
    TreeNode(long id, String name, long parentId, String role) {
        this.companyName = name;
        this.children = new ArrayList<>();
        this.id = id;
        this.parentId = parentId;
        this.parent = null;
        this.role = role;
        this.tree = null;
    }

    @Override
    public String toString(){
        return (role != null) ? id + " " + companyName + " - " + role : id + " " + companyName;
    }

    String getName() {
        return companyName;
    }
    long getId() {
        return id;
    }
    long getParentId() {
        return parentId;
    }
    String getRole() {
        return role;
    }
    String getParentName() {
        if (parent != null)
            return parent.getName();
        else return "Null";

    }
    ArrayList<TreeNode> getChildren() {
        return children;
    }
    private CompanyTree getTree(){
        return tree;
    }

    void setTree(CompanyTree t){
        this.tree = t;
    }
    void setParent(TreeNode parent) {
        this.parent = parent;
    }

    TreeNode compareTo(TreeNode node) {
        if (this.companyName.equals(node.getName())) {
            return node;
        } else return null;
    }

    @Deprecated
    ArrayList<ArrayList<CompanyTree>> getRelatedTrees(ArrayList<CompanyTree> ambest){
        ArrayList<ArrayList<CompanyTree>> results = new ArrayList<>();

        results.add(new ArrayList<>());
        results.add(new ArrayList<>());
        results.get(0).add(this.getTree());

        for (CompanyTree amTree: ambest) {
            ArrayList<String> tempNodes = amTree.getCompanyNames();
            for (TreeNode company : tree.getNodes()) {
                if (tempNodes.contains(company.getName())){
                    results.get(1).add(amTree);
                    break;
                }
            }
        }
        return results;
    }

    ArrayList<ArrayList<CompanyTree>> betterGetRelatedTrees(ArrayList<CompanyTree> riskmatch, ArrayList<CompanyTree> ambest){

        //set up return queue
        ArrayList<ArrayList<CompanyTree>> results = new ArrayList<>();
        results.add(new ArrayList<>());
        results.add(new ArrayList<>());
        results.get(0).add(this.getTree());


        //add trees to search to the queues
        Queue<CompanyTree> rmQueue = new LinkedList<>();
        Queue<CompanyTree> amQueue = new LinkedList<>();
        rmQueue.add(this.getTree());

        //check all trees until both queues are empty
        while(!rmQueue.isEmpty() || !amQueue.isEmpty()){
            CompanyTree rmTree = rmQueue.poll();
            if(rmTree != null){ //checking for amTrees that match any nodes in given risk match tree
                for (CompanyTree amTree: ambest) {//search through each am best tree
                    ArrayList<String> tempNodes = amTree.getCompanyNames();//get all the nodes from the am best tree
                    for (TreeNode company : rmTree.getNodes()) {//loop through
                        if (tempNodes.contains(company.getName())){//if there is a match between the company we're searching for and the company we have, then proceed
                            if(!results.get(1).contains(amTree)) {//if the company has never been added to result set, add to it and the queue for future checking
                                results.get(1).add(amTree);
                                amQueue.add(amTree);
                            }

                        }
                    }
                }
            }

            CompanyTree amTree = amQueue.poll();
            if(amTree != null){ //checking for amTrees that match any nodes in given risk match tree
                for (CompanyTree rmT: riskmatch) {//search through each am best tree
                    ArrayList<String> tempNodes = rmT.getCompanyNames();//get all the nodes from the am best tree
                    for (TreeNode company : amTree.getNodes()) {//loop through
                        if (tempNodes.contains(company.getName())){//if there is a match between the company we're searching for and the company we have, then proceed
                            if(!results.get(0).contains(rmT)) {//if the company has never been added to result set, add to it and the queue for future checking
                                results.get(0).add(rmT);
                                rmQueue.add(rmT);
                            }

                        }
                    }
                }
            }
        }

        return results;
    }
}
