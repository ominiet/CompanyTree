import java.util.ArrayList;

class TreeNode {
    private String companyName;
    private long id;
    private long parentId;
    private TreeNode parent;
    private ArrayList<TreeNode> children;
    private String role;
    private CompanyTree tree;

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

    TreeNode compareTo(TreeNode node) {
        if (this.companyName.equals(node.getName())) {
            return node;
        } else return null;
    }
    @Override
    public String toString(){
        return (role != null) ? id + " " + companyName + " - " + role : id + " " + companyName;
    }

    String getName() {
        return companyName;
    }

    ArrayList<TreeNode> getChildren() {
        return children;
    }

    long getId() {
        return id;
    }

    long getParentId() {
        return parentId;
    }

    String getParentName() {
        if (parent != null)
            return parent.getName();
        else return "Null";

    }

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

    void setParent(TreeNode parent) {
        this.parent = parent;
    }

    String getRole() {
        return role;
    }
    void setTree(CompanyTree t){
        this.tree = t;
    }
    CompanyTree getTree(){
        return tree;
    }
}
