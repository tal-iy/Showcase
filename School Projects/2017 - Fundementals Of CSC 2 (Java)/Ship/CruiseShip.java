package ship;
/*
    @author v.shydlonok

    CruiseShip class extends the Ship class.
    Uses maxPassengers int to keep track of how
        many passengers the cruise ship can carry.
    Defines a getter and setter for the maxPassengers
        variable.
*/
public class CruiseShip extends Ship 
{
    protected int maxPassengers;
    
    public CruiseShip()
    {
        super();
        maxPassengers = 0;
    }
    
    public CruiseShip(int maxPassengers,String name,int year)
    {
        super(name,year);
        this.maxPassengers = maxPassengers;
    }

    public int getPassengers() {
        return maxPassengers;
    }

    public void setPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }
}
