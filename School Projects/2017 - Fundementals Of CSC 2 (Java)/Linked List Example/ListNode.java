package linkedlistexample;

/**
 *
 * @author v.shydlonok
 */
public class ListNode <T> {
    private ListNode next;
    private ListNode prev;
    private T value;
    
    ListNode()
    {
        next = null;
        prev = null;
        value = null;
    }
    
    ListNode(ListNode next, ListNode prev, T value)
    {
        this.next = next;
        this.prev = prev;
        this.value = value;
    }

    public void setNext(ListNode next) 
    {
        this.next = next;
    }

    public void setPrev(ListNode prev) 
    {
        this.prev = prev;
    }

    public void setValue(T value) 
    {
        this.value = value;
    }

    public ListNode getNext() 
    {
        return next;
    }

    public ListNode getPrev() 
    {
        return prev;
    }

    public T getValue() 
    {
        return value;
    }
}
