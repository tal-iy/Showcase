package exceptionclasses;

/*
    @author v.shydlonok

    NoLowerCase class is an Exception for handling 
        passwords that don't have at least one 
        lower case letter.
 */

public class NoLowerCase extends Exception{
    
    private String msg;
    
    public NoLowerCase(String msg)
    {
        super(msg);
        this.msg = msg;
    }
}
