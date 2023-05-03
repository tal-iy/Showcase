package exceptionclasses;

/*
    @author v.shydlonok

    NoDigit class is an Exception for handling 
        passwords that don't have at least one 
        digit.
 */

public class NoDigit extends Exception{
    
    private String msg;
    
    public NoDigit(String msg)
    {
        super(msg);
        this.msg = msg;
    }
}
