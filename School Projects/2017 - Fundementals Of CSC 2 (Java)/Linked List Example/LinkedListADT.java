package linkedlistexample;

/**
 *
 * @author v.shydlonok
 */
public class LinkedListADT <T> implements LinkedListInterface
{
    protected ListNode front;
    protected ListNode back;
    protected int length;
    
    public void LinkedListADT()
    {
        front = null;
        back = null;
        length = 0;
    }

    @Override
    public boolean isEmpty() 
    {
        return length == 0;
    }

    @Override
    public int size() 
    {
        return length;
    }

    @Override
    public void addFront(Object element) 
    {
        ListNode<T> n = new ListNode<T>(front, null,(T)element);
        
        if (front == null)
            back = n;
        else
            front.setPrev(n);
        
        front = n;
        length++;
    }

    @Override
    public void addBack(Object element) {
        ListNode<T> n = new ListNode<T>(null, back,(T)element);
        
        if (back == null)
            front = n;
        else
            back.setNext(n);
        
        back = n;
        length++;
    }

    @Override
    public void insertAt(Object element, int index) 
    {
        if (index == 0)
            addFront(element);
        else if (index == length-1)
            addBack(element);
        else if (index >= length || index < 0)
            throw new IndexOutOfBoundsException("List index out of bounds!");
        else
        {
            ListNode<T> n = new ListNode<T>(null, back,(T)element);
            ListNode node = front;
            for(int i=0;i<index;i++)
                node = node.getNext();
            
            n.setPrev(node.getPrev());
            n.setNext(node);
            node.getPrev().setNext(n);
            node.setPrev(n);
            length++;
        }
    }

    @Override
    public void insert(Object element) 
    {
        if (isEmpty())
            addFront(element);
        else
        {
            ListNode<T> n = new ListNode<T>(null, back,(T)element);
            ListNode node = front;
            for(int i=0;i<length;i++)
            {
                node = node.getNext();
                
                if (((Comparable)(node.getValue())).compareTo((Comparable)element) > 0)
                {
                    n.setPrev(node.getPrev());
                    n.setNext(node);
                    node.getPrev().setNext(n);
                    node.setPrev(n);
                    length++;
                }
            }
        }
    }

    @Override
    public boolean deleteFront() 
    {
        boolean result = false;
        if (front != null)
        {
            if (front != back)
            {
                front.getNext().setPrev(null);
                front = front.getNext();
            }
            else
            {
                front = back = null;
            }
            length--;
            result = true;
        }
        return result;
    }

    @Override
    public boolean deleteBack() 
    {
        boolean result = false;
        if (front != null)
        {
            if (front != back)
            {
                back.getPrev().setNext(null);
                back = back.getPrev();
            }
            else
            {
                front = back = null;
            }
            length--;
            result = true;
        }
        return result;
    }

    @Override
    public boolean deleteAt(int index) 
    {
        boolean result = false;
        if (index == 0)
            result = deleteFront();
        else if (index == length-1)
            result = deleteBack();
        else if (index >= length || index < 0)
            throw new IndexOutOfBoundsException("List index out of bounds!");
        else
        {
            ListNode node = front;
            for(int i=0;i<index;i++)
                node = node.getNext();
            
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
            result = true;
            length--;
        }
        return result;
    }

    @Override
    public boolean delete(Object element) 
    {
        boolean result = false;
        if (front.getValue().equals(element))
            result = deleteFront();
        else if (back.getValue().equals(element))
            result = deleteBack();
        else if (!isEmpty())
        {
            ListNode node = front;
            while(node.getNext() != null && !(node.getValue().equals(element)))
                node = node.getNext();
            
            if (node.getValue().equals(element))
            {
                node.getPrev().setNext(node.getNext());
                node.getNext().setPrev(node.getPrev());
                result = true;
                length--;
            }
        }
        return result;
    }

    @Override
    public Object at(int index) 
    {
        Object temp = null;
        if (index < length && index >= 0)
        {
            ListNode node = front;
            for(int i=0;i<index;i++)
                node = node.getNext();
            temp = node.getValue();
        }
        ListNode n = new ListNode(null, null, temp);
        return n.getValue();
    }

    @Override
    public Object front() 
    {
        Object temp = null;
        if (front != null)
            temp = front.getValue();
        ListNode n = new ListNode(null, null, temp);
        return n.getValue();
    }

    @Override
    public Object back() 
    {
        Object temp = null;
        if (back != null)
            temp = back.getValue();
        ListNode n = new ListNode(null, null, temp);
        return n.getValue();
    }

    @Override
    public void clear() 
    {
        front = null;
        back = null;
        length = 0;
    }

    @Override
    public String toString() 
    {
        String elements = "";
        ListNode<T> current = front;
        while(current != null)
        {
            elements += "   "+current.getValue()+"\n";
            current = current.getNext();
        }
        return "LinkedListADT\n{\n" + elements + '}';
    }
}
