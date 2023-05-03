package contactdirectory;

import java.io.*;

/**
 * Manages the saving and loading of data for the ContactDirectory
 * @author v.shydlonok
 */
public class FileManagement implements Serializable
{
    private ContactDirectory directory;
    
    /**
     * Default constructor
     */
    public FileManagement()
    {
        this.directory = null;
    }
    
    /**
     * Constructor with an initial ContactDirectory
     * @param directory ContactDirectory to keep track of
     */
    public FileManagement(ContactDirectory directory)
    {
        this.directory = directory;
    }
    
    /**
     * Returns the ContactDirectory that is being used
     * @return ContactDirectory
     */
    public ContactDirectory getDirectory() 
    {
        return directory;
    }
    
    /**
     * Sets the ContactDirectory for saving/loading
     * @param directory ContactDirectory
     */
    public void setDirectory(ContactDirectory directory) 
    {
        this.directory = directory;
    }
    
    /**
     * Saves the data within the working ContactDirectory to a file
     * @param fileName file name of the save file
     * @throws IOException 
     */
    public void save(String fileName) throws IOException
    {
        // Initialize file and object output streams
        FileOutputStream fos = new FileOutputStream(fileName); 
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        
        // Write data to the stream
        oos.writeObject(this);
        oos.flush();
    }

    /**
     * Loads a serialized FileManagement object from the specified file.
     * @param fileName the file from which the FileManagement object is read
     * @return the loaded FileManagement object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static FileManagement load(String fileName) throws IOException, ClassNotFoundException
    {
        // Initialize file input stream
        FileInputStream fis = new FileInputStream(fileName);
        FileManagement fm;
        
        // Try to read the file and load the serialized FileManagement object
        try (ObjectInputStream ois = new ObjectInputStream(fis)) {
            fm = (FileManagement) ois.readObject();
        }

        return fm;
    }
}
