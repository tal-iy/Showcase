package electronics;
/*
    @author v.shydlonok

    WallClock class extends the Clock class.
    Overrides room() method, returns "Dining Room".
    Overrides toString(), prints Clock toString().
*/
public class WallClock extends Clock
{
    public WallClock(String eManufacturer,double ePrice,double eWeight,String time)
    {
        super(eManufacturer,ePrice,eWeight,time);
    }
    
    @Override
    public String room()
    {
        return "Dining Room";
    }

    @Override
    public String toString() {
        return "WallClock{" + super.toString() + '}';
    }
}
