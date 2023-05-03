package binarytree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author v.shydlonok
 * @param <T>
 */
public class BinaryTree<T extends Comparable> implements BinaryTreeADT<T>
{
    private TreeNode root;
    
    public BinaryTree()
    {
        root = null;
    }
    
    public BinaryTree(T element)
    {
        root = new TreeNode<>(element);
    }

    @Override
    public T getRootElement() 
    {
        T temp = null;
        
        if (root != null)
            temp = (T)root.getElement();
        
        return temp;
    }
    
    public void addElement(T element)
    {
        root = addElement(root, element);
    }
    
    private TreeNode<T> addElement(TreeNode<T> node, T element)
    {
        if (node == null)
            node = new TreeNode<>(element);
        else if (element.compareTo(node.getElement()) < 0)
            node.setLeft(addElement(node.getLeft(), element));
        else
            node.setRight(addElement(node.getRight(), element));
        
        return node;
    }
    
    @Override
    public String toString()
    {
        ArrayList<T> tempList = new ArrayList<>();
        inOrder(root, tempList);
        return tempList.toString();
    }

    @Override
    public boolean isEmpty() 
    {
        return root == null;
    }

    @Override
    public int size() 
    {
        int count = 0;
        if (root != null)
            count = root.numChildren() + 1;
        return count;
    }

    @Override
    public boolean contains(T element) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T find(T element) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<T> iterator() 
    {
        return iteratorInOrder();
    }

    @Override
    public Iterator<T> iteratorInOrder() 
    {
        ArrayList<T> tempList = new ArrayList<>();
        inOrder(root, tempList);
        return new TreeIterator(tempList.iterator());
    }
    
    private void inOrder(TreeNode<T> node, ArrayList<T> list)
    {
        if (node != null)
        {
            inOrder(node.getLeft(), list);
            list.add(node.getElement());
            inOrder(node.getRight(), list);
        }
    }

    @Override
    public Iterator<T> iteratorPreOrder() 
    {
        ArrayList<T> tempList = new ArrayList<>();
        preOrder(root, tempList);
        return new TreeIterator(tempList.iterator());
    }
    
    private void preOrder(TreeNode<T> node, ArrayList<T> list)
    {
        if (node != null)
        {
            list.add(node.getElement());
            preOrder(node.getLeft(), list);
            preOrder(node.getRight(), list);
        }
    }

    @Override
    public Iterator<T> iteratorPostOrder() 
    {
        ArrayList<T> tempList = new ArrayList<>();
        postOrder(root, tempList);
        return new TreeIterator(tempList.iterator());
    }
    
    private void postOrder(TreeNode<T> node, ArrayList<T> list)
    {
        if (node != null)
        {
            postOrder(node.getLeft(), list);
            postOrder(node.getRight(), list);
            list.add(node.getElement());
        }
    }
    
    private class TreeIterator implements Iterator
    {
        private Iterator<T> iter;
        
        public TreeIterator(Iterator<T> iter)
        {
            this.iter = iter;
        }
        
        @Override
        public boolean hasNext() 
        {
            return (iter.hasNext());
        }

        @Override
        public Object next() 
        {
            if (hasNext())
                return iter.next();
            else
               throw new NoSuchElementException();
        }
    }
    
}
