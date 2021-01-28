package ship;
/*
    @author v.shydlonok

    CargoShip class extends the Ship class.
    Uses capacity int to keep track of the capacity of 
        the cargo ship.
    Defines a getter and setter for the capacity variable.
*/
public class CargoShip extends Ship 
{
    protected int capacity;
    
    public CargoShip()
    {
        super();
        capacity = 0;
    }
    
    public CargoShip(int capacity,String name,int year)
    {
        super(name,year);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
