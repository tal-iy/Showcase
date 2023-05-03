package pkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// compile from WEB-INF: javac -cp ../../../lib/servlet-api.jar -d classes src/pkg/*.java
// package from shop: jar -cvf shop.war *
// move war: mv shop.war ../

// list: curl --cookie cookies.txt --cookie-jar cookies.txt 127.0.0.1:2761/shop/cart/list
// add: curl --cookie cookies.txt --cookie-jar cookies.txt -d 'id=123&name=Item1&price=10.00' 127.0.0.1:2761/shop/cart/add
// delete: curl --cookie cookies.txt --cookie-jar cookies.txt -d 'id=123' 127.0.0.1:2761/shop/cart/delete
// purchase: curl --cookie cookies.txt --cookie-jar cookies.txt -d '' 127.0.0.1:2761/shop/cart/purchase
// clear: curl --cookie cookies.txt --cookie-jar cookies.txt -d '' 127.0.0.1:2761/shop/cart/clear

public class CartServlet extends HttpServlet {

    public static boolean DEBUG = true;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Create a temporary cart database
        File cartsFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"carts.dat");
        if (cartsFile.createNewFile()) {

            // Copy all carts to the temporary database from the WAR file
            String line;
            FileWriter copyWriter = new FileWriter(cartsFile);
            BufferedReader copyReader = new BufferedReader(new InputStreamReader(getServletContext().getResourceAsStream("/WEB-INF/carts.dat")));
            while ((line = copyReader.readLine()) != null)
            copyWriter.write(line + "\n");
            copyWriter.close();
            copyReader.close();

        }

        try {

            String command = request.getPathInfo();
            if (command.equals("/reset") && DEBUG) {
                cartsFile.delete();
            } else if (command.equals("/test") && DEBUG) {
                String line;
                BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(cartsFile)));
                while ((line = copyReader.readLine()) != null)
                    out.println(line);
                copyReader.close();
            } else if (command.equals("/list")) {

                // Make sure that there is a logged in user
                if (request.getSession(false) != null) {

                    // Get the username associated with the account
                    HttpSession session = request.getSession();
                    String loggedUser = (String)session.getAttribute("user");

                    // Read the carts database until all are read or the user is found
                    String line;
                    BufferedReader cartsReader = new BufferedReader(new InputStreamReader(new FileInputStream(cartsFile)));
                    while((line = cartsReader.readLine()) != null && !line.equals(loggedUser)){};

                    // Make sure a user was found
                    if (loggedUser.equals(line)) {

                        out.println("------------Cart for "+loggedUser+"------------");

                        // Print all items in the user's cart
                        int numItems = Integer.parseInt(cartsReader.readLine());
                        if (numItems == 0) out.println("empty");
                        for(int i=0; i<numItems; i++) {
                            out.println("Item ID: "+cartsReader.readLine());
                            out.println("Item Name: "+cartsReader.readLine());
                            out.println("Item Price: "+cartsReader.readLine()+"\n");
                        }

                        cartsReader.close();
                    } else { // Write a new cart entry for a new user

                        cartsReader.close();

                        // Add a new cart to the database file
                        BufferedWriter usersWriter = new BufferedWriter(new FileWriter(cartsFile, true));
                        usersWriter.write(loggedUser);
                        usersWriter.newLine();
                        usersWriter.write("0");
                        usersWriter.close();

                        out.println("------------Cart for "+loggedUser+"------------");
                        out.println("empty");

                    }

                } else out.println("Must be logged in to do that!");

            } else out.println("Unknown command!");

        } finally {
            out.close();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Create a temporary cart database
        File cartsFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"carts.dat");
        if (cartsFile.createNewFile()) {

            // Copy all carts to the temporary database from the WAR file
            String line;
            FileWriter copyWriter = new FileWriter(cartsFile);
            BufferedReader copyReader = new BufferedReader(new InputStreamReader(getServletContext().getResourceAsStream("/WEB-INF/carts.dat")));
            while ((line = copyReader.readLine()) != null)
                copyWriter.write(line + "\n");
            copyWriter.close();
            copyReader.close();

        }

        try {

            String command = request.getPathInfo();
            if (command.equals("/reset") && DEBUG) {
                cartsFile.delete();
            } else if (command.equals("/test") && DEBUG) {
                String line;
                BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(cartsFile)));
                while ((line = copyReader.readLine()) != null)
                    out.println(line);
                copyReader.close();
            } else if (command.equals("/add")) {

                // Make sure that there is a logged in user
                if (request.getSession(false) != null) {

                    // Get the username associated with the account
                    HttpSession session = request.getSession();
                    String loggedUser = (String)session.getAttribute("user");

                    // Create a temporary database copy
                    File copyFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"carts_copy.dat");
                    copyFile.createNewFile();

                    // Copy all carts to the database copy
                    String line;
                    FileWriter copyWriter = new FileWriter(copyFile);
                    BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(cartsFile)));
                    while ((line = copyReader.readLine()) != null) {
                        if (loggedUser.equals(line)) {

                            // Rewrite the username
                            copyWriter.write(line + "\n");

                            // Read the number of items already in the cart
                            int numItems = Integer.parseInt(copyReader.readLine());

                            // Update the item count
                            copyWriter.write((numItems+1) + "\n");

                            // Insert the new item
                            copyWriter.write(request.getParameter("id") + "\n");
                            copyWriter.write(request.getParameter("name") + "\n");
                            copyWriter.write(request.getParameter("price") + "\n");

                        }
                        else copyWriter.write(line + "\n");
                    }
                    copyWriter.close();
                    copyReader.close();

                    cartsFile.delete();
                    copyFile.renameTo(cartsFile);

                    out.println("Added item: "+request.getParameter("name")+"!");

                } else out.println("Must be logged in to do that!");

            } else if (command.equals("/delete")) {

                // Make sure that there is a logged in user
                if (request.getSession(false) != null) {

                    // Get the username associated with the account
                    HttpSession session = request.getSession();
                    String loggedUser = (String)session.getAttribute("user");


                    // Create a temporary database copy
                    File copyFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"carts_copy.dat");
                    copyFile.createNewFile();

                    // Copy all carts to the database copy
                    String line;
                    FileWriter copyWriter = new FileWriter(copyFile);
                    BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(cartsFile)));
                    while ((line = copyReader.readLine()) != null) {
                        if (loggedUser.equals(line)) {

                            // Rewrite the username
                            copyWriter.write(line + "\n");

                            // Read the number of items already in the cart
                            int numItems = Integer.parseInt(copyReader.readLine());

                            // Update the item count
                            copyWriter.write((numItems-1) + "\n");

                            for (int i=0; i<numItems; i++) {
                                line = copyReader.readLine();
                                if (!request.getParameter("id").equals(line)) {
                                    copyWriter.write(line + "\n"); // ID
                                    copyWriter.write(copyReader.readLine() + "\n"); // Name
                                    copyWriter.write(copyReader.readLine() + "\n"); // Price
                                } else {
                                    copyReader.readLine(); // Skip name
                                    copyReader.readLine(); // Skip price
                                }
                            }

                        }
                        else copyWriter.write(line + "\n");
                    }
                    copyWriter.close();
                    copyReader.close();

                    cartsFile.delete();
                    copyFile.renameTo(cartsFile);

                    out.println("Deleted item ID:"+request.getParameter("id")+"!");

                } else out.println("Must be logged in to do that!");

            } else if (command.equals("/purchase")) {

                // Make sure that there is a logged in user
                if (request.getSession(false) != null) {

                    // Get the username associated with the account
                    HttpSession session = request.getSession();
                    String loggedUser = (String)session.getAttribute("user");

                    // Storage for cart info
                    int itemCount = 0;
                    double itemPrice = 0;

                    // Create a temporary database copy
                    File copyFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"carts_copy.dat");
                    copyFile.createNewFile();

                    // Copy all carts to the database copy
                    String line;
                    FileWriter copyWriter = new FileWriter(copyFile);
                    BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(cartsFile)));
                    while ((line = copyReader.readLine()) != null) {
                        if (loggedUser.equals(line)) {

                            // Rewrite the username
                            copyWriter.write(line + "\n");

                            // Increment the item count
                            int numItems = Integer.parseInt(copyReader.readLine());
                            itemCount += numItems;

                            // Update the item count to 0
                            copyWriter.write("0\n");

                            // Skip all items in the cart
                            for (int i=0; i<numItems; i++) {
                                copyReader.readLine(); // ID
                                copyReader.readLine(); // Name
                                itemPrice += Double.parseDouble(copyReader.readLine()); // Price
                            }

                        }
                        else copyWriter.write(line + "\n");
                    }
                    copyWriter.close();
                    copyReader.close();

                    cartsFile.delete();
                    copyFile.renameTo(cartsFile);

                    out.println("Purchased "+itemCount+" items for a total of $"+itemPrice+"!");

                } else out.println("Must be logged in to do that!");

            } else if (command.equals("/clear")) {

                // Make sure that there is a logged in user
                if (request.getSession(false) != null) {

                    // Get the username associated with the account
                    HttpSession session = request.getSession();
                    String loggedUser = (String)session.getAttribute("user");


                    // Create a temporary database copy
                    File copyFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"carts_copy.dat");
                    copyFile.createNewFile();

                    // Copy all carts to the database copy
                    String line;
                    FileWriter copyWriter = new FileWriter(copyFile);
                    BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(cartsFile)));
                    while ((line = copyReader.readLine()) != null) {
                        if (loggedUser.equals(line)) {

                            // Rewrite the username
                            copyWriter.write(line + "\n");

                            // Skip the item count
                            int numItems = Integer.parseInt(copyReader.readLine());

                            // Update the item count to 0
                            copyWriter.write("0\n");

                            // Skip all items in the cart
                            for (int i=0; i<numItems; i++) {
                                copyReader.readLine(); // ID
                                copyReader.readLine(); // Name
                                copyReader.readLine(); // Price
                            }
                        }
                        else copyWriter.write(line + "\n");
                    }
                    copyWriter.close();
                    copyReader.close();

                    cartsFile.delete();
                    copyFile.renameTo(cartsFile);

                    out.println("Cart cleared!");

                } else out.println("Must be logged in to do that!");

            } else out.println("Unknown command!");

        } finally {
            out.close();
        }
    }
}
