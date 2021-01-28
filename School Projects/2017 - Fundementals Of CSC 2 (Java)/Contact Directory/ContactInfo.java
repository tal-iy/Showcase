package contactdirectory;

import java.io.Serializable;

/**
 * Keeps track of contact information about a person
 * @author v.shydlonok
 */
public class ContactInfo implements Serializable 
{
    private String name;
    private String address;
    private String email;
    private String workPhone;
    private String cellPhone;
    
    /**
     * Default constructor for initializing: name, address, email, workPhone, cellPhone
     */
    public ContactInfo() 
    {
        this.name = "";
        this.address = "";
        this.email = "";
        this.workPhone = "";
        this.cellPhone = "";
    }
        
    /**
     * Constructor for initializing a ContactInfo with specific information
     * @param name
     * @param address
     * @param email
     * @param workPhone
     * @param cellPhone 
     */
    public ContactInfo(String name, String address, String email, String workPhone, String cellPhone) 
    {
        this.name = name;
        this.address = address;
        this.email = email;
        this.workPhone = workPhone;
        this.cellPhone = cellPhone;
    }
    
    /**
     * Returns the name stores in the ContactInfo
     * @return name
     */
    public String getName() 
    {
        return name;
    }
    
    /**
     * Returns the address stores in the ContactInfo
     * @return name
     */
    public String getAddress() 
    {
        return address;
    }
    
    /**
     * Returns the email stores in the ContactInfo
     * @return email
     */
    public String getEmail() 
    {
        return email;
    }
    
    /**
     * Returns the work phone stores in the ContactInfo
     * @return work phone
     */
    public String getWorkPhone() 
    {
        return workPhone;
    }
    
    /**
     * Returns the cell phone stores in the ContactInfo
     * @return cell phone
     */
    public String getCellPhone() 
    {
        return cellPhone;
    }
    
    /**
     * Sets the name information
     * @param name name
     */
    public void setName(String name) 
    {
        this.name = name;
    }
    
    /**
     * Sets the address information
     * @param address address
     */
    public void setAddress(String address) 
    {
        this.address = address;
    }
    
    /**
     * Sets the email information
     * @param email email
     */
    public void setEmail(String email) 
    {
        this.email = email;
    }
    
    /**
     * Sets the work phone information
     * @param workPhone work phone
     */
    public void setWorkPhone(String workPhone) 
    {
        this.workPhone = workPhone;
    }
    
    /**
     * Sets the cell phone information
     * @param cellPhone cell phone
     */
    public void setCellPhone(String cellPhone) 
    {
        this.cellPhone = cellPhone;
    }

    /**
     * Converts information within the ContactInfo to a String
     * @return info string
     */
    @Override
    public String toString() {
        return "ContactInfo{" + "name=" + name + ", address=" + address + ", email=" + email + ", workPhone=" + workPhone + ", cellPhone=" + cellPhone + '}';
    }
}
