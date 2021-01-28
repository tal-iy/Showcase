package electronics;
/*
    @author v.shydlonok

    AlarmClock class extends the Clock class.
    Uses alarmTime String to keep track of time.
    Overrides room() method, returns "Bed Room".
    Overrides toString(), prints Clock toString() 
        along with this.alarmTime.
*/
public class AlarmClock extends Clock
{
    
    protected String alarmTime;
    
    public AlarmClock(String eManufacturer,double ePrice,double eWeight,String time,String aTime)
    {
        super(eManufacturer,ePrice,eWeight,time);
        alarmTime = aTime;
    }
    
    @Override
    public String room()
    {
        return "Bed Room";
    }

    public void setAlarm(String aTime)
    {
        alarmTime = aTime;
    }

    @Override
    public String toString() {
        return "AlarmClock{" + "alarmTime=" + alarmTime + ", " + super.toString() +'}';
    }
}
