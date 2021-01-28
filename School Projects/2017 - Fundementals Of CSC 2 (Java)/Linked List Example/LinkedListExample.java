package linkedlistexample;

/**
 *
 * @author v.shydlonok
 */
public class LinkedListExample 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        LinkedListADT<Integer> myList = new LinkedListADT();
        
        System.out.println(myList);
        System.out.println("Size: "+myList.size());
        System.out.println("Empty: "+myList.isEmpty());
        
        myList.addFront(100);
        myList.addFront(75);
        myList.addBack(80);
        myList.addBack(400);
        myList.addFront(350);
        myList.addBack(200);
        myList.addFront(250);
        
        myList.deleteFront();
        myList.deleteBack();
        
        System.out.println(myList);
        System.out.println("Size: "+myList.size());
        System.out.println("Empty: "+myList.isEmpty());
        
        System.out.println(myList.at(3));
    }
    
}
