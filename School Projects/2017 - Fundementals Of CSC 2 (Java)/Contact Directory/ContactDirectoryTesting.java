package contactdirectory;

import java.io.IOException;

/**
 * Tests ContactDirectory class by populating it
 * with contacts, saving the database to a file,
 * and loading the database from the file.
 * @author v.shydlonok
 */
public class ContactDirectoryTesting {
    
    /**
     * Tests the saving functionality of the FileManagementClass
     */
    public static void testSave()
    {
        ContactDirectory cd;
        FileManagement fm;
        cd = new ContactDirectory();
        fm = new FileManagement(cd);
        
        // Populate contacts list
        cd.addContact("Test1","Add1","Em1","1232","4565");
        cd.addContact("Test2","Add2","Em2","1233","4566");
        cd.addContact("Test3","Add3","Em3","1234","4567");
        
        cd.displayList();
        
        // Try to save the database
        try
        {
            fm.save("test.dat");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Tests the loading functionality of the FileManagementClass
     */
    public static void testLoad()
    {
        ContactDirectory cd;
        FileManagement fm;
        
        // Try to load the database and display it
        try
        {
            fm = FileManagement.load("test.dat");
            cd = fm.getDirectory();
            cd.displayList();
        }
        catch(IOException | ClassNotFoundException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // Saving of data:
        testSave();
        
        // Loading of data:
        testLoad();
    }
}
