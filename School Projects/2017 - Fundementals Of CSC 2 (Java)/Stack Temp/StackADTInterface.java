package stacktemp;

/**
 *
 * @author v.shydlonok
 * @param <T>
 */
public interface StackADTInterface<T>
{
    //put element on top of stack
    //returns true if success
    public boolean push(T element);
    
    //returns and removes top element on stack
    //returns null if stack is empty
    public T pop();
    
    //returns top element on stack
    public T peek();
    
    //returns true if stack is empty
    public boolean isEmpty();
    
    //returns the number of elements on the stack
    public int size();
    
    @Override
    public String toString();
}
