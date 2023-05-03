package linkedlistsorting;

/**
 *
 * @author speciosr
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        Integer [] data = new Integer[10];
        
        data[0] = 2;
        data[1] = 8;
        data[2] = 1;
        data[3] = 22;
        data[4] = 234;
        data[5] = 654;
        data[6] = 43;
        data[7] = 7;
        data[8] = 900;
        data[9] = 1000;

        Display(data);
        BubbleSort(data);
        Display(data);
    }
    
    public static void Display(Integer[] values)
    {
        for(int index=0; index < values.length; index++)
        {
            System.out.println(values[index]);
        }
    }
    
    public static void SelectionSort(Integer [] values)
    {
        int lowestIndex;
        Integer temp;
        //Integer lowestValue;
        
        for(int index = 0; index < values.length-1; index++)
        {
            //lowestValue = values[index];
            lowestIndex = index;
            for(int index2 = index+1; index2 < values.length; index2++)
            {
                if(values[index2] < values[lowestIndex]) // lowestValue)
                {
                    //lowestValue = values[index2];
                    lowestIndex = index2;
                }
            }
            temp = values[index];
            values[index] = values[lowestIndex];
            values[lowestIndex] = temp;
        }
    }
    
    public static void InsertionSort(Integer [] values)
    {
        Integer key;
        int position;
        
        for(int index = 1; index < values.length; index++)
        {
            key = values[index];
            position = index;
            while(position > 0 && values[position-1] > key)
            {
                values[position] = values[position-1];
                position--;
            }
            values[position] = key;
        }
    }
    
       
    public static void BubbleSort(Integer[] data)
    {
        int position, scan;
        //Integer temp;
        int counter = 0;
        boolean swapped = true;
        
        for (position =  data.length - 1; position >= 0 && swapped; position--)
        {
            swapped = false;
            for (scan = 0; scan <= position - 1; scan++)
            {
                if (data[scan].compareTo(data[scan+1]) > 0)
                {
                    swap(data, scan, scan + 1);
                    swapped = true;
                }
            }
            counter++;
        }
        
        System.out.println("Number of passes " + counter);
    }
    
    private static void swap(Integer[] data, int index1, int index2)
    {
        Integer temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }		

}
