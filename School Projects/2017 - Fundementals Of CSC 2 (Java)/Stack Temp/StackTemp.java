package stacktemp;

/**
 *
 * @author v.shydlonok
 */
public class StackTemp 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        StackADT<Integer> myStack = new StackADT();
        System.out.println(myStack.size());
        System.out.println(myStack.isEmpty());
        myStack.push(12);
        myStack.push(23);
        myStack.push(34);
        myStack.push(45);
        System.out.println(myStack.toString());
        System.out.println(myStack.size());
        System.out.println(myStack.isEmpty());
        System.out.println(myStack.pop());
        System.out.println(myStack.size());
        System.out.println(myStack.isEmpty());
        
        Integer temp;
        for (int i=0;i<7;i++)
        {
            temp = myStack.pop();
            if (temp == null)
                System.out.println("Stack is empty!");
            else
                System.out.println(temp);
        }
    }
    
}
