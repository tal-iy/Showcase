package electronics;
/*
    @author v.shydlonok

    Clock class extends the HomeAppliance class.
    Uses currentTime String to keep track of time.
    Overrides room() method, returns "Living Room".
    Overrides toString(), prints HomeAppliance toString() 
        along with this.currentTime.
*/
public class Clock extends HomeAppliance
{
    protected String currentTime;
    
    public Clock(String eManufacturer,double ePrice,double eWeight,String time)
    {
        super(eManufacturer,ePrice,eWeight);
        currentTime = time;
    }
    
    @Override
    public String room()
    {
        return "Living Room";
    }

    public String getTime() {
        return currentTime;
    }

    public void setTime(String newTime) {
        this.currentTime = newTime;
    }

    @Override
    public String toString() {
        return "Clock{" + "currentTime=" + currentTime + ", " + super.toString() + '}';
    }
}
