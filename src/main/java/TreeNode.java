import java.util.ArrayList;

public class TreeNode {
        private String companyName;
        private int id;
        private int parentId;
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

        
}
