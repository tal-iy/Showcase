package linkedlistsorting;
        
/**
 *
 * @author speciosr
 */
public class LinkedListExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        LinkedListADT <Integer> myList = new LinkedListADT<Integer>();
        
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
        
        myList.AddFront(100);
        myList.AddFront(200);
        myList.AddFront(300);
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
        
        System.out.println(myList);
        
        myList.Append(400);
        myList.Append(500);     
        
        System.out.println("Unsorted:");
        System.out.println(myList);
        
        
        
        System.out.println("Sorted:");
        System.out.println(myList);
        
        
        myList.RemoveFront();
        System.out.println(myList);
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
        
        /*while(!myList.IsEmpty())
        {
            myList.RemoveFront();
            System.out.println(myList);
        }
        System.out.println(myList);
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
        myList.RemoveFront();
        System.out.println(myList);
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
*/
        myList.RemoveEnd();
        System.out.println(myList);
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
        
        myList.RemoveEnd();
        System.out.println(myList);
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
        
        myList.RemoveEnd();
        System.out.println(myList);
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
        
        myList.RemoveEnd();
        System.out.println(myList);
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
                
        myList.RemoveEnd();
        System.out.println(myList);
        System.out.println(myList.Size());
        System.out.println(myList.IsEmpty());
        

        
    }
    
}
