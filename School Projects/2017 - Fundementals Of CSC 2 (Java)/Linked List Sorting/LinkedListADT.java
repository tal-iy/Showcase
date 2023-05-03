/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package linkedlistsorting;

/**
 *
 * @author speciosr
 */
public class LinkedListADT <T extends Comparable<T>> implements LinkedListInterface
{
    protected Node front;
    protected Node back;
    protected int size;

    public LinkedListADT() 
    {
        front = back = null;
        size = 0;
    }
    
    public void SelectionSort()
    {
        int lowestIndex;
        T temp;
        //Integer lowestValue;
        
        for(int index = 0; index < Size()-1; index++)
        {
            //lowestValue = values[index];
            lowestIndex = index;
            for(int index2 = index+1; index2 < Size(); index2++)
            {
                if(((T)GetValue(index2)).compareTo((T)GetValue(lowestIndex)) < 0)
                {
                    //lowestValue = values[index2];
                    lowestIndex = index2;
                }
            }
            temp = (T)GetValue(index);
            Replace(index,GetValue(lowestIndex));
            Replace(lowestIndex,temp);
        }
    }
    
    public void InsertionSort()
    {
        T key;
        int position;
        
        for(int index = 1; index < Size(); index++)
        {
            key = (T)GetValue(index);
            position = index;
            while(position > 0 && values[position-1] > key)
            {
                values[position] = values[position-1];
                position--;
            }
            values[position] = key;
        }
    }
    
    public void Replace(int index, Object obj)
    {
        Node temp = front;
        for(int i=0;i<index;i++)
            temp = temp.getNext();
        temp.setValue(obj);
    }
        
    @Override
    public boolean IsEmpty() {
        boolean result = false;
        
        if (size == 0)
            result = true;
        
        return result;
    }
    
    @Override
    public int Size() {
        return size;
    }

    /*
    Check if back exist
        Create temp node
        Set temp node next to null
        Set temp node prev back
        Set back node next to temp node
        Set back to the temp node
    else no back
        Create temp node
        Set front to temp node
        Set back to the temp node
        Set next to null in temp node
        Set prev to null in temp node
    end if
    increment size
    */
    @Override
    public void Append(Object obj) 
    {
        if(back != null)
        {
            Node<T> temp = new Node<T>(null, back, (T)obj);
            back.setNext(temp);
            back = temp;
            size++;
        }
        else
        {
            AddFront(obj);
        }
    }

    /*
    Check if front exist
        Create temp node
        Set temp node next to front
        Set temp node prev null
        Set front node prev to temp node
        Set front to the temp node
    else no front
        Create temp node
        Set front to temp node
        Set back to the temp node
        Set next to null in temp node
        Set prev to null in temp node
    end if
    increment size
    */
    @Override
    public void AddFront(Object obj) 
    {      
        if(front != null)
        {
            Node<T> temp = new Node<T>(front, null, (T)obj);
            front.setPrevious(temp);
            front = temp;
        }
        else
        {
            Node<T> temp = new Node<T>(null, null, (T)obj);
            back = front = temp;            
        }        
        size++;
        
    }

    @Override
    public void Insert(Object obj, int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Insert(Object obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*
        Check if list is not empty
            if front equals back 
                set front and back to null
            else 
                Create temp node 
                Set temp refernecer to back prev
                Set temp next to null
                Set back prev to null
                Set back to temp                
            End IF
            decrement size
        End If
    */
    @Override
    public boolean RemoveEnd() 
    {
        boolean result = false;
        
        if(!(IsEmpty()))
        {
            if(front == back)
            {
                back = front = null;
            }
            else
            {
                Node temp = back.getPrevious();
                temp.setNext(null);
                back.setPrevious(null);
                back = temp;
            }
            size--;  
            result = true;
        }
        
        return result;
    }

    /*
        Check if list is not empty
            if front equal to back
                Set front to null
                Set back to null
            else
                Create temp null reference
                set temp to front next
                set temp prev to null
                set front next to null
                set front to temp
            end if
            decrement size
        end if    
    */
    @Override
    public boolean RemoveFront() 
    {
        boolean result = false;
        
        if(!(IsEmpty()))
        {
            if(front == back)
            {
                back = front = null;
            }
            else
            {
                Node temp = front.getNext();
                temp.setPrevious(null);
                front.setNext(null);
                front = temp;
            }
            size--;  
            result = true;
        }
        
        return result;
    }

    @Override
    public boolean RemoveAt(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // find object and remove it
    /*
        if list is empty noting to remove
        else
            Loop until you find the object or reach end of list
    */
    @Override
    public boolean Remove(Object obj) 
    {
        boolean result = false;
        
        if (!IsEmpty())
        {
            Node temp, temp2, temp3;   

            if(obj.equals(front.getValue()))
                result = RemoveFront();
            else if (obj.equals(back.getValue()))
                result = RemoveEnd();
            else
            {
                temp = front;            
                while(temp.getNext() != null &&
                        !(temp.getValue().equals(obj)))
                {
                    temp = temp.getNext();
                }
                if (temp.getValue().equals(obj))
                {
                    temp2 = temp.getPrevious();
                    temp3 = temp.getNext();
                    
                    temp2.setNext(temp3);
                    temp3.setPrevious(temp2);
                    
                    temp.setNext(null);
                    temp.setPrevious(null); 
                    size--;
                    result = true;
                }
            } 
        }
        return result;
    }

    @Override
    public Object GetValue(int index) 
    {
        Node temp, temp2;
        Object obj = null;
        
        if(index < size && index >= 0)
        {
            temp = front;
            for(int count=1; count <= index; count++)
            {
                temp = temp.getNext();
            }
            temp2 = new Node(null, null, temp.getValue());
            obj = temp2.getValue();
        }
        
        return obj;
    }

    @Override
    public Object GetValueEnd() {
        Node temp;
        Object obj = null;
        
        if(back != null)
        {
            temp = new Node(null, null, back.getValue());
            obj = temp.getValue();
        }
        
        return obj; 
                
    }

    @Override
    public Object GetValueFront() {
        return front.getValue();
    }

    @Override
    public String toString() {
        String list = "";
        
        list = "LinkedListADT{\n";
        Node current = front;
	while (current != null)
	{
	    list += current.getValue() + "\n";
	    current = current.getNext();
	}
        list += "}";
        
        return list;
    }
    
    
}
