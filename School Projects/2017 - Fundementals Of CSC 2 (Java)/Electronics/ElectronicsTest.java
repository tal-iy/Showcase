package electronics;
/*
    @author v.shydlonok

    ElectronicsTest class contains the main method.
    Tests all Electronics to make sure they behave correctly.
*/
public class ElectronicsTest 
{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        IPod ipod = new IPod("Apple", 249.00, 0.2, 32,"6th Generation");
        CellPhone cell = new CellPhone("Motorola", 99.99, .5,"555-987-1829", true);
        Printer printer = new Printer("HP", 145.87, 8.0, 1080, false,false);
        Clock clock = new Clock("Centurian", 20.34, 3.2, "12:35pm");
        AlarmClock aClock = new AlarmClock("Timex", 18.99, 2.3, "1:37pm","6:35am");
        WallClock wClock = new WallClock("Swiss", 13.89, 2.5, "2:33pm");
        
        System.out.println("IPod\n");
        ipod.setCapacity(64);
        ipod.setType("7th Generation");
        System.out.println(ipod.toString() + "\n");
        System.out.println("Cell Phone\n");
        System.out.println(cell.toString());
        
        if (cell.takesPictures())
            System.out.println("This is a camera phone.\n");
        else
            System.out.println("Sorry, your phone cannot take pictures.\n");
        
        System.out.println("Printer\n");
        System.out.println(printer.toString());
        
        if (printer.printerStatus())
            System.out.println("This printer is ready to print.\n");
        else
            System.out.println("Printer not ready. Load ink and paper.\n");
        
        System.out.println("Clock\n");
        System.out.println(clock.toString() + "\n");
        System.out.println("Wall Clock\n");
        System.out.println("Current time: " + wClock.getTime());
        System.out.println("New time will be set to 3:45pm.");
        wClock.setTime("3:45pm");
        System.out.println(wClock.toString() + "\n");
        System.out.println("Alarm Clock\n");
        System.out.println("Current time: " + aClock.getTime());
        System.out.println("New time will match wall clock.");
        aClock.setTime(wClock.getTime());
        System.out.println(aClock.toString() + "\n");
    }
}
