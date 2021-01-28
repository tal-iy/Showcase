package pkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import java.util.*;

// compile from WEB-INF: javac -cp ../../../lib/servlet-api.jar -d classes src/pkg/*.java
// package from shop: jar -cvf shop.war *
// move war: mv shop.war ../

// categories: curl --cookie cookies.txt --cookie-jar cookies.txt 127.0.0.1:2761/shop/catalog/categories
// items: curl --cookie cookies.txt --cookie-jar cookies.txt '127.0.0.1:2761/shop/catalog/items?category=Category1'
// details: curl --cookie cookies.txt --cookie-jar cookies.txt '127.0.0.1:2761/shop/catalog/details?id=1'

public class CatalogServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        BufferedReader in = req.getReader();

        String command = req.getPathInfo();
        if (command == null || command.equals("/")) {

            // Read request body
            String body = "";
            String line;
            try {
                while ((line = in.readLine()) != null)
                    body += line + "\n";
            } catch (IOException ex) {
            } finally {
                in.close();
            }

            JSONObject jsonObject = new JSONObject(body);

            // Parse json
            String authToken = jsonObject.getString("authToken");
            String name = jsonObject.getString("name");

            // Make sure it's the admin
            if (authToken.equals("ADMIN_TOKEN")) {

                // Retrieve catalog list
                Integer nextId;
                if ((nextId = (Integer)req.getServletContext().getAttribute("catalog_nextId")) == null)
                    nextId = 0;
                ArrayList<DatabaseTypes.CatalogCategory> categories;
                if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>)req.getServletContext().getAttribute("catalog")) == null)
                    categories = new ArrayList<>();

                // Create a new category
                categories.add(new DatabaseTypes.CatalogCategory(name, nextId));

                // Send json response with category ID and name
                JSONObject jCategory = new JSONObject();
                jCategory.put("name", name);
                jCategory.put("id", nextId);
                out.println(jCategory.toString());

                res.setStatus(HttpServletResponse.SC_CREATED);

                nextId++;

                // Save category list
                req.getServletContext().setAttribute("catalog_nextId",nextId);
                req.getServletContext().setAttribute("catalog",categories);
            }
        } else {
            String[] tokens = command.split("/");
            if (tokens.length == 3 && tokens[2].equals("items")) {

                // Try to convert the categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);

                    // Read request body
                    String body = "";
                    String line;
                    try {
                        while ((line = in.readLine()) != null)
                            body += line + "\n";
                    } catch (IOException ex) {
                    } finally {
                        in.close();
                    }

                    JSONObject jsonObject = new JSONObject(body);

                    // Parse json
                    String authToken = jsonObject.getString("authToken");
                    String name = jsonObject.getString("name");
                    double price = jsonObject.getDouble("price");
                    String desc = jsonObject.getString("desc");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {

                        // Retrieve catalog list
                        ArrayList<DatabaseTypes.CatalogCategory> categories;
                        if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>)req.getServletContext().getAttribute("catalog")) == null || categories.size() == 0) {

                            // Category list empty
                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 404: Not found!");
                        } else {

                            DatabaseTypes.CatalogCategory category = null;
                            for (DatabaseTypes.CatalogCategory cat : categories) {
                                if (cat.id == categoryId) {
                                    category = cat;
                                    break;
                                }
                            }

                            if (category == null) {

                                // Category doesn't exist
                                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                res.setContentType("text/html;charset=UTF-8");
                                out.println("ERROR 404: Not found!");
                            } else {

                                // Add new item to the category
                                DatabaseTypes.CartItem item = new DatabaseTypes.CartItem(name, category.cart_nextId, price, desc, categoryId);
                                category.cart.add(item);
                                category.cart_nextId++;

                                // Send json response
                                JSONObject jItem = new JSONObject();
                                jItem.put("name", item.name);
                                jItem.put("id", item.id);
                                jItem.put("category", item.category);
                                jItem.put("price", item.price);
                                jItem.put("desc", item.description);
                                out.println(jItem.toString());

                                res.setStatus(HttpServletResponse.SC_CREATED);
                            }
                        }

                        // Save category list
                        req.getServletContext().setAttribute("catalog",categories);

                    } else {

                        // Not admin
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 403: Forbidden!");
                    }

                } catch (NumberFormatException ex) {

                    // categoryId is supposed to be an integer value
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }
            } else {

                // Too many tokens in the URl
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 400: Bad request!");
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        BufferedReader in = req.getReader();

        String command = req.getPathInfo();
        if (command == null || command.equals("/")) {

            // Retrieve catalog list
            ArrayList<DatabaseTypes.CatalogCategory> categories;
            if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>)req.getServletContext().getAttribute("catalog")) == null || categories.size() == 0) {

                // Category list empty
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 404: Not found!");
            } else {

                // Build json response
                JSONArray jArray = new JSONArray();
                for (DatabaseTypes.CatalogCategory category : categories) {
                    JSONObject jCategory = new JSONObject();
                    jCategory.put("name", category.name);
                    jCategory.put("id", category.id);
                    jArray.put(jCategory);
                }

                JSONObject jResponse = new JSONObject();
                jResponse.put("categories",jArray);
                out.println(jResponse.toString());
            }

        } else {
            String[] tokens = command.split("/");
            if (tokens.length == 3 && tokens[2].equals("items")) {

                // Try to convert the categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);

                    // Retrieve catalog list
                    ArrayList<DatabaseTypes.CatalogCategory> categories;
                    if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>)req.getServletContext().getAttribute("catalog")) == null || categories.size() == 0) {

                        // Category list empty
                        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 404: Not found!");
                    } else {

                        boolean found = false;
                        for (DatabaseTypes.CatalogCategory category : categories) {
                            if (category.id == categoryId) {
                                if (category.cart.size() != 0) {
                                    // Build json response
                                    JSONArray jArray = new JSONArray();
                                    for (DatabaseTypes.CartItem item : category.cart) {
                                        JSONObject jItem = new JSONObject();
                                        jItem.put("name", item.name);
                                        jItem.put("id", item.id);
                                        jItem.put("price", item.price);
                                        jArray.put(jItem);
                                    }

                                    JSONObject jResponse = new JSONObject();
                                    jResponse.put("category", category.name);
                                    jResponse.put("id", category.id);
                                    jResponse.put("items", jArray);
                                    out.println(jResponse.toString());

                                    found = true;
                                }
                                break;
                            }
                        }

                        if (!found) {

                            // Category doesn't exist
                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 404: Not found!");
                        }
                    }

                } catch (NumberFormatException ex) {

                    // categoryId is supposed to be an integer value
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }

            } else if (tokens.length == 4 && tokens[2].equals("items")) {

                // Try to convert the itemId and categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);
                    int itemId = Integer.parseInt(tokens[3]);

                    // Retrieve catalog list
                    ArrayList<DatabaseTypes.CatalogCategory> categories;
                    if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>)req.getServletContext().getAttribute("catalog")) == null || categories.size() == 0) {

                        // Category list empty
                        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 404: Not found!");
                    } else {

                        boolean found = false;
                        for (DatabaseTypes.CatalogCategory category : categories) {
                            if (category.id == categoryId) {
                                for (DatabaseTypes.CartItem item : category.cart) {
                                    JSONObject jItem = new JSONObject();
                                    jItem.put("category", categoryId);
                                    jItem.put("name", item.name);
                                    jItem.put("id", item.id);
                                    jItem.put("price", item.price);
                                    jItem.put("desc", item.description);
                                    out.println(jItem.toString());

                                    found = true;
                                    break;
                                }
                            }

                            if (found)
                                break;
                        }

                        if (!found) {

                            // Category doesn't exist
                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 404: Not found!");
                        }
                    }

                } catch (NumberFormatException ex) {

                    // itemId and categoryId are supposed to be integer values
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }
            } else {

                // Too many tokens in the URl
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 400: Bad request!");
            }
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        BufferedReader in = req.getReader();

        String command = req.getPathInfo();
        if (command != null && !command.equals("/")) {

            String[] tokens = command.split("/");
            if (tokens.length == 2) {

                // Try to convert the categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);

                    // Read request body
                    String body = "";
                    String line;
                    try {
                        while ((line = in.readLine()) != null)
                            body += line + "\n";
                    } catch (IOException ex) {
                    } finally {
                        in.close();
                    }

                    JSONObject jsonObject = new JSONObject(body);

                    // Parse json
                    String authToken = jsonObject.getString("authToken");
                    String name = jsonObject.getString("name");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {

                        // Retrieve catalog list
                        ArrayList<DatabaseTypes.CatalogCategory> categories;
                        if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>)req.getServletContext().getAttribute("catalog")) == null || categories.size() == 0) {

                            // Category list empty
                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 404: Not found!");
                        } else {

                            boolean found = false;
                            for (DatabaseTypes.CatalogCategory category : categories) {
                                if (category.id == categoryId) {

                                    // Change the category name
                                    category.name = name;

                                    // Build json response
                                    JSONObject jCategory = new JSONObject();
                                    jCategory.put("name", category.name);
                                    jCategory.put("id", category.id);
                                    out.println(jCategory.toString());

                                    // Save category list
                                    req.getServletContext().setAttribute("catalog",categories);

                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {

                                // Category doesn't exist
                                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                res.setContentType("text/html;charset=UTF-8");
                                out.println("ERROR 404: Not found!");
                            }
                        }
                    } else {

                        // Not admin
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 403: Forbidden!");
                    }

                } catch (NumberFormatException ex) {

                    // categoryId is supposed to be an integer value
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }

            } else if (tokens.length == 4 && tokens[2].equals("items")) {

                // Try to convert the categoryId to an integer
                try {

                    int categoryId = Integer.parseInt(tokens[1]);
                    int itemId = Integer.parseInt(tokens[3]);

                    // Read request body
                    String body = "";
                    String line;
                    try {
                        while ((line = in.readLine()) != null)
                            body += line + "\n";
                    } catch (IOException ex) {
                    } finally {
                        in.close();
                    }

                    JSONObject jsonObject = new JSONObject(body);

                    // Parse json
                    String authToken = jsonObject.getString("authToken");
                    String name = jsonObject.getString("name");
                    double price = jsonObject.getDouble("price");
                    String desc = jsonObject.getString("desc");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {

                        // Retrieve catalog list
                        ArrayList<DatabaseTypes.CatalogCategory> categories;
                        if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>) req.getServletContext().getAttribute("catalog")) == null || categories.size() == 0) {

                            // Category list empty
                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 404: Not found!");
                        } else {

                            boolean found = false;
                            for (DatabaseTypes.CatalogCategory category : categories) {
                                if (category.id == categoryId) {
                                    for (DatabaseTypes.CartItem item : category.cart) {
                                        if (item.id == itemId) {

                                            // Change the item
                                            item.name = name;
                                            item.price = price;
                                            item.description = desc;

                                            // Build json response
                                            JSONObject jItem = new JSONObject();
                                            jItem.put("category", categoryId);
                                            jItem.put("name", item.name);
                                            jItem.put("id", item.id);
                                            jItem.put("price", item.price);
                                            jItem.put("desc", item.description);
                                            out.println(jItem.toString());

                                            // Save category list
                                            req.getServletContext().setAttribute("catalog", categories);

                                            found = true;
                                            break;
                                        }
                                    }

                                    if (found)
                                        break;
                                }
                            }

                            if (!found) {

                                // Category doesn't exist
                                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                res.setContentType("text/html;charset=UTF-8");
                                out.println("ERROR 404: Not found!");
                            }
                        }
                    } else {

                        // Not admin
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 403: Forbidden!");
                    }

                } catch (NumberFormatException ex) {

                    // itemId and categoryId are supposed to be integer values
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }
            } else {

                // Too many tokens in the URl
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 400: Bad request!");
            }
        } else {

            // Can't do PUT on the category list
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            res.setContentType("text/html;charset=UTF-8");
            out.println("ERROR 405: Method not allowed!");
        }
    }


    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        BufferedReader in = req.getReader();

        String command = req.getPathInfo();
        if (command != null && !command.equals("/")) {

            String[] tokens = command.split("/");
            if (tokens.length == 2) {

                // Try to convert the categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);

                    // Read request body
                    String body = "";
                    String line;
                    try {
                        while ((line = in.readLine()) != null)
                            body += line + "\n";
                    } catch (IOException ex) {
                    } finally {
                        in.close();
                    }

                    JSONObject jsonObject = new JSONObject(body);

                    // Parse json
                    String authToken = jsonObject.getString("authToken");
                    String name = jsonObject.getString("name");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {

                        // Retrieve catalog list
                        ArrayList<DatabaseTypes.CatalogCategory> categories;
                        if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>)req.getServletContext().getAttribute("catalog")) == null || categories.size() == 0) {

                            // Category list empty
                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 404: Not found!");
                        } else {

                            boolean found = false;
                            for (DatabaseTypes.CatalogCategory category : categories) {
                                if (category.id == categoryId) {

                                    // Remove the category
                                    categories.remove(category);

                                    // Build json response
                                    JSONObject jCategory = new JSONObject();
                                    jCategory.put("name", category.name);
                                    jCategory.put("id", category.id);
                                    out.println(jCategory.toString());

                                    // Save category list
                                    req.getServletContext().setAttribute("catalog",categories);

                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {

                                // Category doesn't exist
                                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                res.setContentType("text/html;charset=UTF-8");
                                out.println("ERROR 404: Not found!");
                            }
                        }
                    } else {

                        // Not admin
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 403: Forbidden!");
                    }

                } catch (NumberFormatException ex) {

                    // categoryId is supposed to be an integer value
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }

            } else if (tokens.length == 4 && tokens[2].equals("items")) {

                // Try to convert the categoryId to an integer
                try {

                    int categoryId = Integer.parseInt(tokens[1]);
                    int itemId = Integer.parseInt(tokens[3]);

                    // Read request body
                    String body = "";
                    String line;
                    try {
                        while ((line = in.readLine()) != null)
                            body += line + "\n";
                    } catch (IOException ex) {
                    } finally {
                        in.close();
                    }

                    JSONObject jsonObject = new JSONObject(body);

                    // Parse json
                    String authToken = jsonObject.getString("authToken");
                    String name = jsonObject.getString("name");
                    double price = jsonObject.getDouble("price");
                    String desc = jsonObject.getString("desc");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {

                        // Retrieve catalog list
                        ArrayList<DatabaseTypes.CatalogCategory> categories;
                        if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>) req.getServletContext().getAttribute("catalog")) == null || categories.size() == 0) {

                            // Category list empty
                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 404: Not found!");
                        } else {

                            boolean found = false;
                            for (DatabaseTypes.CatalogCategory category : categories) {
                                if (category.id == categoryId) {
                                    for (DatabaseTypes.CartItem item : category.cart) {
                                        if (item.id == itemId) {

                                            // Remove the item from the catalog
                                            category.cart.remove(item);

                                            // Build json response
                                            JSONObject jItem = new JSONObject();
                                            jItem.put("category", categoryId);
                                            jItem.put("name", item.name);
                                            jItem.put("id", item.id);
                                            jItem.put("price", item.price);
                                            jItem.put("desc", item.description);
                                            out.println(jItem.toString());

                                            // Save category list
                                            req.getServletContext().setAttribute("catalog", categories);

                                            found = true;
                                            break;
                                        }
                                    }

                                    if (found)
                                        break;
                                }
                            }

                            if (!found) {

                                // Category doesn't exist
                                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                res.setContentType("text/html;charset=UTF-8");
                                out.println("ERROR 404: Not found!");
                            }
                        }
                    } else {

                        // Not admin
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 403: Forbidden!");
                    }

                } catch (NumberFormatException ex) {

                    // itemId and categoryId are supposed to be integer values
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }
            } else {

                // Too many tokens in the URl
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 400: Bad request!");
            }
        } else {

            // Can't do PUT on the category list
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            res.setContentType("text/html;charset=UTF-8");
            out.println("ERROR 405: Method not allowed!");
        }
    }
}
