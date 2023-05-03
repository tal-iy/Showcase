package contactdirectory;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Keeps track of a list of ContactInfo
 * @author v.shydlonok
 */
public class ContactDirectory implements Serializable
{
    private LinkedList<ContactInfo> list;
    
    /**
     * Constructor for initializing ContactInfo list
     */
    public ContactDirectory() 
    {
        list = new LinkedList<>();
    }
        
    /**
     * Adds a ContactInfo to the list
     * @param contact ContactInfo to add
     */
    public void addContact(ContactInfo contact)
    {
        list.add(contact);
    }
    
    /**
     * Adds a new ContactInfo to the list using seprate parameters
     * @param name
     * @param address
     * @param email
     * @param workPhone
     * @param cellPhone 
     */
    public void addContact(String name, String address, String email, String workPhone, String cellPhone)
    {
        list.add(new ContactInfo(name, address, email, workPhone, cellPhone));
    }
        
    /**
     * Deletes ContactInfo from the list at an index
     * @param index index at which to delete from
     */
    public void deleteContact(int index)
    {
        list.remove(index);
    }
        
    /**
     * Deletes ContactInfo from the list
     * @param contact ContactInfo to delete
     */
    public void deleteContact(ContactInfo contact)
    {
        list.remove(contact);
    }
    
    /**
     * Updates ContactInfo at an index
     * @param index index at which to update
     * @param contact new ContactInfo to replace with
     */
    public void updateContact(int index, ContactInfo contact)
    {
        list.set(index, contact);
    }
        
    /**
     * Updates ContactInfo at an index
     * @param index index at which to update
     * @param name
     * @param address
     * @param email
     * @param workPhone
     * @param cellPhone 
     */
    public void updateContact(int index, String name, String address, String email, String workPhone, String cellPhone)
    {
        ContactInfo contact = list.get(index);
        contact.setName(name);
        contact.setAddress(address);
        contact.setEmail(email);
        contact.setWorkPhone(workPhone);
        contact.setCellPhone(cellPhone);
    }
        
    /**
     * Searches for a ContactInfo in the list
     * @param contact ContactInfo to search for
     * @return index at which the ContactInfo is found, -1 if not found
     */
    public int findContact(ContactInfo contact)
    {
        return list.indexOf(contact);
    }
        
    /**
     * Displays the ContactInfo at an index
     * @param index index of the ContactInfo
     */
    public void displayContact(int index)
    {
        System.out.println(list.get(index));
    }
        
    /**
     * Displays the whole list of ContactInfo
     */
    public void displayList()
    {
        // Iterate through the list and print each ContactInfo element
        for(ContactInfo contact:list)
            System.out.println(contact);
    }
}
