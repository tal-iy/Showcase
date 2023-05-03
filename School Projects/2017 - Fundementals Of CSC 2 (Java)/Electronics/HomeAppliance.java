package electronics;
/*
    @author v.shydlonok

    HomeAppliance class extends the Electronics class.
    Defines a new method room(), which returns the room
        that it's supposed to be in.
*/
public class HomeAppliance extends Electronics
{
    public HomeAppliance(String eManufacturer,double ePrice,double eWeight)
    {
        super(eManufacturer,ePrice,eWeight);
    }
    
    public String room()
    {
        return "Living Room";
    }
}
