package exceptionclasses;

/*
    @author v.shydlonok

    NoUpperCase class is an Exception for handling 
        passwords that don't have at least one 
        upper case letter.
 */

public class NoUpperCase extends Exception{
    
    private String msg;
    
    public NoUpperCase(String msg)
    {
        super(msg);
        this.msg = msg;
    }
}
