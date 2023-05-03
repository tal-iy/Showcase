package binarytreeexample;

import binarytree.BinaryTree;
import java.util.Iterator;

/**
 *
 * @author v.shydlonok
 */
public class BinaryTreeExample 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        BinaryTree<Integer> intTree = new BinaryTree<>();
        
        if (intTree.isEmpty())
            System.out.println("Empty!");
        else
            System.out.println("Not empty!");
        
        System.out.println("Tree size: "+intTree.size());
        System.out.println(intTree.getRootElement());
        
        intTree.addElement(5);
        intTree.addElement(7);
        intTree.addElement(3);
        intTree.addElement(4);
        
        System.out.println("Tree size: "+intTree.size());
        System.out.println(intTree.getRootElement());
        
        if (intTree.isEmpty())
            System.out.println("Empty!");
        else
            System.out.println("Not empty!");
        
        Iterator iter;
        Integer temp;
        
        iter = intTree.iterator();
        System.out.println("In Order Output");
        while(iter.hasNext())
        {
            temp = (Integer)iter.next();
            System.out.println(temp);
        }
        
        iter = intTree.iteratorPreOrder();
        System.out.println("Pre Order Output");
        while(iter.hasNext())
        {
            temp = (Integer)iter.next();
            System.out.println(temp);
        }
        
        iter = intTree.iteratorPostOrder();
        System.out.println("Post Order Output");
        while(iter.hasNext())
        {
            temp = (Integer)iter.next();
            System.out.println(temp);
        }
        
        System.out.println(intTree);
    }
}
