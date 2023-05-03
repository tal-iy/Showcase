package binarytree;

import java.util.Iterator;

/**
 *
 * @author v.shydlonok
 */
public interface BinaryTreeADT<T>
{
    public T getRootElement();
    
    public boolean isEmpty();
    
    public int size();
    
    public boolean contains(T element);
    
    public T find(T element);
    
    @Override
    public String toString();
    
    public Iterator<T> iterator();
    
    public Iterator<T> iteratorInOrder();
    
    public Iterator<T> iteratorPreOrder();
    
    public Iterator<T> iteratorPostOrder();
    
}
