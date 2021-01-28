package electronics;
/*
    @author v.shydlonok

    Electronics class, stores a manufacturer String,
        a price double, and a weight double.
    Its constructor sets all three variables.
    Overrides toString(), printing this.manufacturer,
        this.price, and this.weight.
        
*/
public class Electronics 
{
    protected String manufacturer;
    protected double price;
    protected double weight;
    
    public Electronics(String eManufacturer,double ePrice,double eWeight)
    {
        manufacturer = eManufacturer;
        price = ePrice;
        weight = eWeight;
    }

    @Override
    public String toString() {
        return "Electronics{" + "manufacturer=" + manufacturer + ", price=" + price + ", weight=" + weight + '}';
    }
}
