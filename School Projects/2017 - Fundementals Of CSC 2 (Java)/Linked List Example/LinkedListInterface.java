package linkedlistexample;

/**
 *
 * @author v.shydlonok
 * @param <T>
 */
public interface LinkedListInterface <T> 
{
    public boolean isEmpty();
    
    public int size();
    
    public void addFront(T element);
    
    public void addBack(T element);
    
    //add to list at index
    public void insertAt(T element, int index);
    
    //keeping a sorted list
    public void insert(T element);
    
    
    public boolean deleteFront();
    
    public boolean deleteBack();
    
    //delete element at index
    public boolean deleteAt(int index);
    
    //delete first occurence of element
    public boolean delete(T element);
    
    public T at(int index);
    
    public T front();
    
    public T back();
    
    public void clear();
}
