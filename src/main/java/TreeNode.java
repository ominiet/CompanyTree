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
        TreeNode(String id, String name, String parentId){
            this.companyName = name;
            this.children = new ArrayList<TreeNode>();
            try {
                this.id = Integer.parseInt(id);
                this.parentId= Integer.parseInt(parentId);
            } catch (NumberFormatException nfe){
                this.id = 0;
                this.parentId=0;
            }
        }
        public void printNode(){
            System.out.println(id + ": " + companyName);
        }
        public String getName(){
            return companyName;
        }
        public ArrayList<TreeNode> getChildren() {return children;}
        public int getId(){ return id;}
        public int getParentId() {return parentId;}


}
