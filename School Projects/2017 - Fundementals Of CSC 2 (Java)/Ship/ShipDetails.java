package ship;
/*
    @author v.shydlonok

    ShipDetails interface, needs to be 
        implemented by classes that store 
        ship information.
*/
public interface ShipDetails {
    
    public void DisplayShipDetails();
    public boolean isOlder(ShipDetails ship);
    
}
