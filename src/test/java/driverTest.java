import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class driverTest {

    @Test
    public void test(){


    ArrayList<TreeNode> a= new ArrayList<>();

        TreeNode one = new TreeNode(1,"one",1);
        TreeNode two = new TreeNode(2,"two",1);
        TreeNode three = new TreeNode(3,"three",2);

        a.add(one);
        a.add(two);
        a.add(three);


        CompanyTree tree1 = new CompanyTree(one);
        tree1.BuildTree(a);

        ArrayList<CompanyTree> forest1 = new ArrayList<>();
        ArrayList<CompanyTree> forest2 = new ArrayList<>();
        forest1.add(tree1);
        forest2.add(tree1);

        forest1.get(0).printRecursive();
        forest2.get(0).printRecursive();



        assertEquals(1.0,driver.Similarity(forest1,forest2),.001);


    }
}