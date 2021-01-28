package linkedlistsorting;

/**
 *
 * @author speciosr
 */
public class Node <T>
{
    private Node next;
    private Node previous;
    private T value;

    public Node() 
    {
        next = null;
        previous = null;
        value = null;
    }

    public Node(Node next, Node previous, T value) {
        this.next = next;
        this.previous = previous;
        this.value = value;
    }    

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setValue(T value) {
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }

    
}
