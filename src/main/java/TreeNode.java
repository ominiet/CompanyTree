import java.util.ArrayList;

class TreeNode {
        private String companyName;
        private long id;
        private long parentId;
        private TreeNode parent;
        private ArrayList<TreeNode> children;

        TreeNode(long id, String name, long parentId){
            this.companyName = name;
            this.children = new ArrayList<>();
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


        String getName(){
            return companyName;
        }
        ArrayList<TreeNode> getChildren() {
            return children;
        }
        long getId(){
            return id;
        }
        long getParentId() {
            return parentId;
        }
        String getParentName(){
            if (parent != null)
            return parent.getName();
            else return "Null";

        }
        void setParent(TreeNode parent){
            this.parent = parent;
        }
}
