import java.util.ArrayList;

public class TreeNode {
        private String companyName;
        private int id;
        private int parentId;
        private TreeNode parent;
        private ArrayList<TreeNode> children;

        TreeNode() {
            this.companyName = "unset";
            this.id = 0000;
            this.children = new ArrayList<TreeNode>();
        }
        TreeNode(int id, String name, int parentId){
            this.companyName = name;
            this.children = new ArrayList<TreeNode>();
            this.id = id;
            this.parentId =parentId;
            this.parent = null;
        }
        TreeNode compareTo(TreeNode node){
            if(this.companyName.equals(node.getName())){

                return node;
            }
            else return null;
        }

        public void printNode(){
            System.out.println(id + ": " + companyName);
        }
        public String getName(){
            return companyName;
        }
        public ArrayList<TreeNode> getChildren() {
            return children;
        }
        public int getId(){
            return id;
        }
        public int getParentId() {
            return parentId;
        }
        public String getParentName(){
            if (parent != null)
            return parent.getName();
            else return "Null";

        }
        public void setParent(TreeNode parent){
            this.parent = parent;
        }

}
