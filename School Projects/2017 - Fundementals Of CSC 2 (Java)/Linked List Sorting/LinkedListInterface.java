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
public interface LinkedListInterface <T>
{
    //done
    public boolean IsEmpty();
    //done
    public int Size();
    //done
    public void Append(T obj);
    //done
    public void AddFront(T obj);
    
    // adding to list at any location
    // if index is zero then adding to front
    // If index great than size then either
    //      Append to the end of the list
    //      Or throw exception that index is out of bounds
    //      Or return boolean where false means index beyond size
    //          of list
    public void Insert(T obj, int index);
    
    // Keeping a sorted list
    // Obj must have compareTo method
    // If parameter is Comparable object it forces the object 
    //  to be implementing the compare to interface
    public void Insert(T obj);
    
    //done
    public boolean RemoveEnd();
    //done
    public boolean RemoveFront();
    
    // remove object at index
    // if index is zero
    //      remove from front
    // if index greater than size
    //      Throw exception
    // Loop to the index 
    //      Remove node
    public boolean RemoveAt(int index);
    
    // find object and remove it
    /*
        if list is empty noting to remove
        else
            Loop until you find the object or reach end of list
    */
    public boolean Remove(T obj);
    
    
    //done
    public T GetValue(int index);
    
    //done
    public T GetValueEnd();
    //done
    public T GetValueFront();
    
}
