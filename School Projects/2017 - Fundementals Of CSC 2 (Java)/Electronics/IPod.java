package electronics;
/*
    @author v.shydlonok

    IPod class extends the PortableElectronics class.
    Uses capacity int to keep track of storage capacity.
    Uses type String to keep track of the iPod type.
    Overrides batteryType() method, returns "Lithium Ion".
    Overrides toString(), prints PortableElectronics toString() 
        along with this.capacity and this.type.
*/
public class IPod extends PortableElectronics
{
    protected int capacity;
    protected String type;
    
    public IPod(String eManufacturer,double ePrice,double eWeight,int capacity,String type)
    {
        super(eManufacturer,ePrice,eWeight);
        this.capacity = capacity;
        this.type = type;
    }
    
    @Override
    public String batteryType()
    {
        return "Lithium Ion";
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "IPod{" + "capacity=" + capacity + ", type=" + type + ", " + super.toString() + '}';
    }
}
