package exceptionclasses;

/*
    @author v.shydlonok

    PasswordTooShort class is an Exception for handling 
        passwords that are shorter than 8 characters.
 */

public class PasswordTooShort extends Exception{
    
    private String msg;
    
    public PasswordTooShort(String msg)
    {
        super(msg);
        this.msg = msg;
    }
}
