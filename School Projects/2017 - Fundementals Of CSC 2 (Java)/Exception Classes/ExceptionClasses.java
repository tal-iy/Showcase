package exceptionclasses;

import java.util.Scanner;

/*
    @author v.shydlonok

    ExceptionClasses prompts for a password string,
        then validates it to make sure it is at
        least 8 characters long, has at least one
        lower case and one upper case letter, and
        has at least one digit. If the validation 
        fails, prompts for another password.
 */

public class ExceptionClasses 
{

    public static void main(String[] args) 
    {
        boolean valid = false;
        String pass;
        Scanner scan = new Scanner(System.in);
        
        System.out.print("Enter a password to validate: ");
        pass = scan.nextLine();
        
        //loop until a valid password is entered
        while(!valid)
        {
            //validate the entered password
            try
            {
                valid = ValidatePassword(pass);
            }
            //catch all possibly thrown exceptions by ValidatePassword
            catch(NoDigit | NoLowerCase | NoUpperCase | PasswordTooShort exception)
            {
                System.out.println(exception.getMessage());
            }
            
            //prompt for a new password if validation failed
            if (!valid)
            {
                System.out.print("\nEnter a new password to validate: ");
                pass = scan.nextLine();
            }
        }
        
        System.out.println("The password "+pass+" is valid!");
    }
    
    public static boolean ValidatePassword(String word) throws PasswordTooShort, NoLowerCase, NoUpperCase, NoDigit
    {
        //check for valid length
        if (word.length() < 8)
            throw new PasswordTooShort("Password must be at least 8 characters long.");
        
        //check for a lower case letter
        if (word.toUpperCase().equals(word))
            throw new NoLowerCase("Password must have at least one lower case letter.");
        
        //check for an upper case letter
        if (word.toLowerCase().equals(word))
            throw new NoUpperCase("Password must have at least one upper case letter.");
        
        //check for at least one digit
        boolean digitFound = false;
        for(int i=0;i<10;i++)
            if (word.contains(Integer.toString(i)))
                digitFound = true;
        if (!digitFound)
            throw new NoDigit("Password must have at least one digit.");
        
        //password is valid if program reaches this point
        return true;
    }
    
}
