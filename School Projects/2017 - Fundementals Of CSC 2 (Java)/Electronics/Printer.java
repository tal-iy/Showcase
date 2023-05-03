package electronics;
/*
    @author v.shydlonok

    Printer class extends the HomeAppliance class.
    Uses dotsPerInch int to keep track of the printers precision.
    Uses paperLoaded boolean to keep track of whether it has paper.
    Uses inkLoaded boolean to keep track of whether it has ink.
    Defines a new method printerStatus(), which returns whether
    the printer can print (has paper and ink).
*/
public class Printer extends HomeAppliance
{
    private int dotsPerInch;
    private boolean paperLoaded;
    private boolean inkLoaded;
    
    public Printer(String eManufacturer,double ePrice,double eWeight,int dpi,boolean paper,boolean ink)
    {
        super(eManufacturer,ePrice,eWeight);
        dotsPerInch = dpi;
        paperLoaded = paper;
        inkLoaded = ink;
    }
    
    public boolean printerStatus()
    {
        return paperLoaded && inkLoaded;
    }
}
