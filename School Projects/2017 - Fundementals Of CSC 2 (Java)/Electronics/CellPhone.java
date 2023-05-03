package electronics;
/*
    @author v.shydlonok

    CellPhone class extends the PortableElectronics class.
    Uses phoneNumber String to keep track of a phone number.
    Uses hasCamera boolean to keep track of whether it can take pictures.
    Overrides batteryType() method, returns "Lithium Ion".
    Overrides toString(), prints PortableElectronics toString() 
        along with this.phoneNumber and this.hasCamera.
*/
public class CellPhone extends PortableElectronics
{
    protected String phoneNumber;
    protected boolean hasCamera;
    
    public CellPhone(String eManufacturer,double ePrice,double eWeight,String number,boolean camera)
    {
        super(eManufacturer,ePrice,eWeight);
        this.phoneNumber = number;
        this.hasCamera = camera;
    }
    
    @Override
    public String batteryType()
    {
        return "Lithium Ion";
    }
    
    public String number()
    {
        return phoneNumber;
    }
    
    public boolean takesPictures()
    {
        return hasCamera;
    }

    @Override
    public String toString() {
        return "CellPhone{" + "phoneNumber=" + phoneNumber + ", hasCamera=" + hasCamera + ", " + super.toString() + '}';
    }
}
