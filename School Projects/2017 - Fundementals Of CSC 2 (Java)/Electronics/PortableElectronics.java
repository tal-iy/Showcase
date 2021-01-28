package electronics;
/*
    @author v.shydlonok

    PortableElectronics class extends the Electronics class.
    Defines a new method batteryType(), which returns the
        type of battery that it uses, "Lithium" as default.
*/
public class PortableElectronics extends Electronics {
    
    public PortableElectronics(String eManufacturer,double ePrice,double eWeight)
    {
        super(eManufacturer,ePrice,eWeight);
    }
    
    public String batteryType()
    {
        return "Lithium";
    }
}
