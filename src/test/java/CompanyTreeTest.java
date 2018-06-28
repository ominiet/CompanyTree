import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CompanyTreeTest {
    @Test
    public void testCompanyTree(){
        ArrayList<TreeNode> list1 = new ArrayList<>();
        list1.add(new TreeNode(1,"one", 1));
        list1.add(new TreeNode(2,"two", 1));
        list1.add(new TreeNode(3,"three", 2));


        CompanyTree tree1 = new CompanyTree(list1.get(0));
        tree1.BuildTree(list1);
        tree1.reorderChildren();

        ArrayList<TreeNode> list2 = new ArrayList<>();
        list2.add(new TreeNode(1,"one", 1));
        list2.add(new TreeNode(2,"two", 1));
        list2.add(new TreeNode(3,"three", 2));

        CompanyTree tree2 = new CompanyTree(list2, list2.get(0).getId());

        assertEquals(tree1.toString(),tree2.toString());
    }
}