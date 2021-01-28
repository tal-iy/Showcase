package pkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// compile from WEB-INF: javac -cp ../../../lib/servlet-api.jar -d classes src/pkg/*.java
// package from shop: jar -cvf shop.war *
// move war: mv shop.war ../

// categories: curl --cookie cookies.txt --cookie-jar cookies.txt 127.0.0.1:2761/shop/catalog/categories
// items: curl --cookie cookies.txt --cookie-jar cookies.txt '127.0.0.1:2761/shop/catalog/items?category=Category1'
// details: curl --cookie cookies.txt --cookie-jar cookies.txt '127.0.0.1:2761/shop/catalog/details?id=1'

public class CatalogServlet extends HttpServlet {

    public static boolean DEBUG = true;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Create a temporary catalog database
        File catalogFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"catalog.dat");
        if (catalogFile.createNewFile()) {

            // Copy all catalog items to the temporary database from the WAR file
            String line;
            FileWriter copyWriter = new FileWriter(catalogFile);
            BufferedReader copyReader = new BufferedReader(new InputStreamReader(getServletContext().getResourceAsStream("/WEB-INF/catalog.dat")));
            while ((line = copyReader.readLine()) != null)
                copyWriter.write(line + "\n");
            copyWriter.close();
            copyReader.close();

        }

        try {

            String command = request.getPathInfo();

            if (command.equals("/reset") && DEBUG) {
                catalogFile.delete();
            } else if (command.equals("/test") && DEBUG) {
                String line;
                BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(catalogFile)));
                while ((line = copyReader.readLine()) != null)
                    out.println(line);
                copyReader.close();
            } else if (command.equals("/categories")) {

                // Read the catalog database until all categories are read
                String line;
                BufferedReader catalogReader = new BufferedReader(new InputStreamReader(new FileInputStream(catalogFile)));
                while((line = catalogReader.readLine()) != null)
                {
                    int numItems = Integer.parseInt(catalogReader.readLine());
                    out.println("Category: "+line);
                    out.println("Items: "+numItems);

                    // Skip all items in the category
                    for(int i=0; i<numItems; i++) {
                        catalogReader.readLine(); // ID
                        catalogReader.readLine(); // Name
                        catalogReader.readLine(); // Price
                        catalogReader.readLine(); // Description
                    }
                }

                catalogReader.close();

            } else if (command.equals("/items")) {

                String category = request.getParameter("category");

                // Read the catalog database until the category is found
                String line;
                BufferedReader catalogReader = new BufferedReader(new InputStreamReader(new FileInputStream(catalogFile)));
                while((line = catalogReader.readLine()) != null && !category.equals(line)) {};

                if (category.equals(line))
                {
                    // Return category info
                    int numItems = Integer.parseInt(catalogReader.readLine());
                    out.println("Category: "+line);
                    out.println("Items: "+numItems+"\n");

                    // Return info for every item
                    for(int i=0; i<numItems; i++) {
                        out.println("ID: "+catalogReader.readLine().replace(':',' '));
                        out.println("Name: "+catalogReader.readLine());
                        out.println("Price: "+catalogReader.readLine());
                        out.println("Description: "+catalogReader.readLine());
                    }

                } else out.println("Category not found!");

                catalogReader.close();

            } else if (command.equals("/details")) {

                String itemID = ":"+request.getParameter("id");

                // Read the catalog database until the item is found
                String line;
                BufferedReader catalogReader = new BufferedReader(new InputStreamReader(new FileInputStream(catalogFile)));
                while((line = catalogReader.readLine()) != null && !itemID.equals(line)) {};

                if (itemID.equals(line))
                {
                    // Return item info
                    out.println("ID: "+line.replace(':',' '));
                    out.println("Name: "+catalogReader.readLine());
                    out.println("Price: "+catalogReader.readLine());
                    out.println("Description: "+catalogReader.readLine());

                } else out.println("Category not found!");

            } else out.println("Unknown command!");

        } finally {
            out.close();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Create a temporary catalog database
        File catalogFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"catalog.dat");
        if (catalogFile.createNewFile()) {

            // Copy all catalog items to the temporary database from the WAR file
            String line;
            FileWriter copyWriter = new FileWriter(catalogFile);
            BufferedReader copyReader = new BufferedReader(new InputStreamReader(getServletContext().getResourceAsStream("/WEB-INF/catalog.dat")));
            while ((line = copyReader.readLine()) != null)
                copyWriter.write(line + "\n");
            copyWriter.close();
            copyReader.close();

        }

        try {

            String command = request.getPathInfo();
            if (command.equals("/reset") && DEBUG) {
                catalogFile.delete();
            } else if (command.equals("/test") && DEBUG) {
                String line;
                BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(catalogFile)));
                while ((line = copyReader.readLine()) != null)
                    out.println(line);
                copyReader.close();
            } else if (command.equals("/add_category")) {

                out.println("Not Implemented!");

            } else if (command.equals("/rename_category")) {

                out.println("Not Implemented!");

            } else if (command.equals("/delete_category")) {

                out.println("Not Implemented!");

            } else if (command.equals("/add_item")) {

                out.println("Not Implemented!");

            } else if (command.equals("/edit_item")) {

                out.println("Not Implemented!");

            } else if (command.equals("/delete_item")) {

                out.println("Not Implemented!");

            } else out.println("Unknown command!");

        } finally {
            out.close();
        }
    }
}
