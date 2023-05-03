package stacktemp;

/**
 *
 * @author v.shydlonok
 * @param <T>
 */
public class StackADT<T> implements StackADTInterface
{
    private T stack[];
    private int capacity = 5;
    private int top = 0;
    
    public StackADT()
    {
        stack = (T[])(new Object[capacity]);
        top = 0;
    }

    @Override
    public boolean push(Object element) 
    {
        boolean result = false;
        if (top < capacity)
        {
            stack[top] = (T)element;
            top ++;
            result = true;
        }
        return result;
    }

    @Override
    public T pop() 
    {
        T obj = null;
        if (!isEmpty())
        {
            top--;
            obj = (T)stack[top];
        }
        return obj;
    }

    @Override
    public T peek() 
    {
        T obj = null;
        if (!isEmpty())
            obj = (T)stack[top-1];
        return obj;
    }

    @Override
    public boolean isEmpty() 
    {
        return top == 0;
    }

    @Override
    public int size() 
    {
        return top;
    }
    
    @Override
    public String toString()
    {
        String str = "";
        for(int i=0;i<top;i++)
            str += stack[i]+", ";
        return str;
    }
}
