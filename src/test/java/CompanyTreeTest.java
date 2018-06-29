import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

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
    @Test
    public void testDifferentTrees(){
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

        CompanyTree tree2 = new CompanyTree(list2, list2.get(0).getId());

        assertNotEquals(tree1.toString(),tree2.toString());
    }
    @Test
    public void testTwoSingleTrees(){


        TreeNode temp = new TreeNode(3441, "ENERGY INSURANCE MUTAL LTD", 0, "CARRIER_NORMAL");
        CompanyTree oldMethod = new CompanyTree(temp);
        oldMethod.BuildTree(new ArrayList<>(Collections.singletonList(temp)));

        TreeNode newTemp = new TreeNode(3441, "ENERGY INSURANCE MUTAL LTD", 0, "CARRIER_NORMAL");
        CompanyTree newMethod = new CompanyTree(new ArrayList<>(Collections.singletonList(newTemp)),3441);

        assertEquals(oldMethod.toString(), newMethod.toString());
    }

    @Test
    public void testNullParent(){
        ArrayList<TreeNode> list1 = new ArrayList<>();
        list1.add(new TreeNode(1234,"1234", 0));
        list1.add(new TreeNode(2,"two", 1234));
        list1.add(new TreeNode(3,"three", 1234));


        CompanyTree tree1 = new CompanyTree(list1.get(0));
        tree1.BuildTree(list1);
        tree1.reorderChildren();

        ArrayList<TreeNode> list2 = new ArrayList<>();
        //list2.add(new TreeNode(1,"one", 1));
        list2.add(new TreeNode(2,"two", 1234));
        list2.add(new TreeNode(3,"three", 1234));

        CompanyTree tree2 = new CompanyTree(list2, 1234);

        assertEquals(tree1.toString(),tree2.toString());
    }
}