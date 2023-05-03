package ship;
/*
    @author v.shydlonok

    Ship class implements the ShipDetails interface.
    Uses name String to keep track of the ships name.
    Uses year int to keep track of the ships year.
    Defines two constructors for setting up default
        values for name and year variables.
    Defines getters and setters for name and year
        variables.
    Implements DisplayShipDetails() method, prints the
        ships name and year that it is from. 
    Implements isOlder(), returns true if the caller 
        ship is older than the ship passed in through
        the parameter, and false otherwise.
*/
public class Ship implements ShipDetails 
{
    protected String name;
    protected int year;
    
    public Ship()
    {
        name = "";
        year = 0;
    }
    
    public Ship(String name,int year)
    {
        this.name = name;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    @Override
    public void DisplayShipDetails() 
    {
        System.out.println("Ship "+getName()+" is from year "+getYear());
    }

    @Override
    public boolean isOlder(ShipDetails ship) 
    {
        boolean older = false;
        if (this.year < ((Ship)ship).year)
            older = true;
        return older;
    }
}
