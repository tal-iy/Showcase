package pkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import java.util.*;

// compile from WEB-INF: javac -cp ../../../lib/servlet-api.jar -d classes src/pkg/*.java
// package from shop: jar -cvf shop.war *
// move war: mv shop.war ../

// list: curl --cookie cookies.txt --cookie-jar cookies.txt 127.0.0.1:2761/shop/cart/list
// add: curl --cookie cookies.txt --cookie-jar cookies.txt -d 'id=123&name=Item1&price=10.00' 127.0.0.1:2761/shop/cart/add
// delete: curl --cookie cookies.txt --cookie-jar cookies.txt -d 'id=123' 127.0.0.1:2761/shop/cart/delete
// purchase: curl --cookie cookies.txt --cookie-jar cookies.txt -d '' 127.0.0.1:2761/shop/cart/purchase
// clear: curl --cookie cookies.txt --cookie-jar cookies.txt -d '' 127.0.0.1:2761/shop/cart/clear

public class UserServlet extends HttpServlet {

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

            // Parse json
            JSONObject jsonObject = new JSONObject(body);
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");

            // Retrieve accounts list
            boolean exists = false;
            ArrayList<DatabaseTypes.Account> accounts;
            if ((accounts = (ArrayList<DatabaseTypes.Account>)req.getServletContext().getAttribute("accounts")) != null) {

                // Look for an account with the same username
                for (DatabaseTypes.Account account : accounts) {
                    if (account.username.equals(username)) {
                        exists = true;

                        // Make sure the passwords match
                        if (account.password.equals(password)) {

                            // Generate a random authToken
                            String authToken = new Long(System.nanoTime()).toString();
                            DatabaseTypes.User user = new DatabaseTypes.User(authToken, account.id);

                            // Add the user to the logged in list
                            HashMap<String, DatabaseTypes.User> users;
                            if ((users = (HashMap<String, DatabaseTypes.User>)req.getServletContext().getAttribute("users")) == null)
                                users = new HashMap();
                            users.put(account.username, user);

                            // Save users list
                            req.getServletContext().setAttribute("users", users);

                            // Send json response with userId and authToken
                            JSONObject jUser = new JSONObject();
                            jUser.put("authToken", authToken);
                            jUser.put("id", account.id);
                            out.println(jUser.toString());

                            res.setStatus(HttpServletResponse.SC_CREATED);

                        } else {

                            // Wrong password
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 403: Forbidden!");
                        }

                        break;
                    }
                }

                // Save accounts list
                req.getServletContext().setAttribute("accounts",accounts);
            }

            if (!exists) {

                // Accounts list empty or account doesn't exist
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 404: Not found!");
            }

        } else {
            String[] tokens = command.split("/");
            if (tokens.length == 3 && tokens[2].equals("items")) {
                try {
                    int userId = Integer.parseInt(tokens[1]);

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

                    // Parse json
                    JSONObject jsonObject = new JSONObject(body);
                    String authToken = jsonObject.getString("authToken");
                    int catalogId = jsonObject.getInt("category");
                    int itemId = jsonObject.getInt("id");

                    // Retrieve list of logged in users
                    HashMap<String, DatabaseTypes.User> users;
                    if ((users = (HashMap<String, DatabaseTypes.User>)req.getServletContext().getAttribute("users")) == null) {

                        // User not logged in
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 401: Authentication needed!");
                    } else {
                        boolean found = false;
                        Iterator it = users.entrySet().iterator();
                        while(it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            DatabaseTypes.User user = (DatabaseTypes.User)entry.getValue();
                            if (user.id == userId) {
                                if (user.authToken.equals(authToken)) {

                                    // Retrieve list of categories
                                    ArrayList<DatabaseTypes.CatalogCategory> categories;
                                    if ((categories = (ArrayList<DatabaseTypes.CatalogCategory>)req.getServletContext().getAttribute("catalog")) != null && categories.size() != 0) {
                                        boolean exists = false;
                                        for (DatabaseTypes.CatalogCategory category : categories) {
                                            if (category.id == catalogId) {

                                                DatabaseTypes.CartItem item = null;
                                                for (DatabaseTypes.CartItem itm : category.cart) {
                                                    if (itm.id == itemId) {
                                                        item = itm;
                                                        break;
                                                    }
                                                }

                                                if (item == null) {

                                                    // Item doesn't exist
                                                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                                    res.setContentType("text/html;charset=UTF-8");
                                                    out.println("ERROR 404: Not found!");
                                                } else {

                                                    // Retrieve accounts list
                                                    ArrayList<DatabaseTypes.Account> accounts;
                                                    if ((accounts = (ArrayList<DatabaseTypes.Account>)req.getServletContext().getAttribute("accounts")) != null) {
                                                        for (DatabaseTypes.Account account : accounts) {
                                                            if (account.id == userId) {

                                                                // Add new item to the account
                                                                DatabaseTypes.CartItem itm = new DatabaseTypes.CartItem(item.name, account.cart_nextId, item.price, item.description, category.id);
                                                                account.cart.add(itm);
                                                                account.cart_nextId++;

                                                                // Build json response
                                                                JSONObject jItem = new JSONObject();
                                                                jItem.put("user", user.id);
                                                                jItem.put("category", itm.category);
                                                                jItem.put("id", itm.id);
                                                                jItem.put("price", itm.price);
                                                                jItem.put("desc", itm.description);
                                                                out.println(jItem.toString());

                                                                break;
                                                            }
                                                        }
                                                    }

                                                    // Save accounts list
                                                    req.getServletContext().setAttribute("accounts",accounts);
                                                }

                                                exists = true;
                                                break;
                                            }
                                        }

                                        if (!exists) {

                                            // Category doesn't exist
                                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                            res.setContentType("text/html;charset=UTF-8");
                                            out.println("ERROR 404: Not found!");
                                        }
                                    } else {

                                        // Category doesn't exist
                                        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                        res.setContentType("text/html;charset=UTF-8");
                                        out.println("ERROR 404: Not found!");
                                    }

                                    found = true;
                                    break;
                                } else {

                                    // Wrong auth token
                                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    res.setContentType("text/html;charset=UTF-8");
                                    out.println("ERROR 401: Authentication needed!");
                                }
                            }
                        }

                        if (!found) {

                            // User not logged in
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 401: Authentication needed!");
                        }
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }
            } else {

                // Can't do POST on an item
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 405: Method not allowed!");
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

            HashMap<String, DatabaseTypes.User> users;
            if ((users = (HashMap<String, DatabaseTypes.User>)req.getServletContext().getAttribute("users")) != null && users.size() != 0) {

                // Build json response
                JSONArray jUsers = new JSONArray();
                Iterator it = users.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry entry = (Map.Entry)it.next();
                    JSONObject jUser = new JSONObject();
                    jUser.put("username", entry.getKey());
                    jUser.put("id", ((DatabaseTypes.User)entry.getValue()).id);
                    jUsers.put(jUser);
                }

                JSONObject jResponse = new JSONObject();
                jResponse.put("users",jUsers);

                out.println(jResponse.toString());
            } else {

                // Logged in users list empty
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 404: Not found!");
            }

        } else {
            String[] tokens = command.split("/");
            if (tokens.length == 3 && tokens[2].equals("items")) {
                try {
                    int userId = Integer.parseInt(tokens[1]);

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

                    // Parse json
                    JSONObject jsonObject = new JSONObject(body);
                    String authToken = jsonObject.getString("authToken");

                    // Retrieve list of logged in users
                    HashMap<String, DatabaseTypes.User> users;
                    if ((users = (HashMap<String, DatabaseTypes.User>)req.getServletContext().getAttribute("users")) == null) {

                        // User not logged in
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 401: Authentication needed!");
                    } else {
                        boolean found = false;
                        Iterator it = users.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            DatabaseTypes.User user = (DatabaseTypes.User) entry.getValue();
                            if (user.id == userId) {
                                if (user.authToken.equals(authToken)) {

                                    // Retrieve accounts list
                                    ArrayList<DatabaseTypes.Account> accounts;
                                    if ((accounts = (ArrayList<DatabaseTypes.Account>)req.getServletContext().getAttribute("accounts")) != null && accounts.size() != 0) {
                                        for (DatabaseTypes.Account account : accounts) {
                                            if (account.id == userId) {
                                                if (account.cart.size() == 0) {

                                                    // Users cart is empty
                                                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                                    res.setContentType("text/html;charset=UTF-8");
                                                    out.println("ERROR 404: Not found!");
                                                } else {

                                                    // Build json response
                                                    JSONArray jArray = new JSONArray();
                                                    for (DatabaseTypes.CartItem item : account.cart) {
                                                        JSONObject jCategory = new JSONObject();
                                                        jCategory.put("name", item.name);
                                                        jCategory.put("category", item.category);
                                                        jCategory.put("id", item.id);
                                                        jCategory.put("price", item.price);
                                                        jArray.put(jCategory);
                                                    }

                                                    JSONObject jResponse = new JSONObject();
                                                    jResponse.put("user", userId);
                                                    jResponse.put("items", jArray);
                                                    out.println(jResponse.toString());
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!found) {

                            // User not logged in
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 401: Authentication needed!");
                        }
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
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
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        BufferedReader in = req.getReader();

        String command = req.getPathInfo();
        if (command != null && !command.equals("/")) {

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

            // Parse json
            JSONObject jsonObject = new JSONObject(body);
            String authToken = jsonObject.getString("authToken");

            String[] tokens = command.split("/");
            if (tokens.length == 2) {

                try {
                    int userId = Integer.parseInt(tokens[1]);

                    HashMap<String, DatabaseTypes.User> users;
                    if ((users = (HashMap<String, DatabaseTypes.User>)req.getServletContext().getAttribute("users")) != null) {

                        boolean found = false;
                        Iterator it = users.entrySet().iterator();
                        while(it.hasNext()) {
                            Map.Entry entry = (Map.Entry)it.next();

                            DatabaseTypes.User user = (DatabaseTypes.User)entry.getValue();

                            if (user.id == userId) {
                                if (authToken.equals("ADMIN_TOKEN") || user.authToken.equals(authToken)) {

                                    users.remove(entry.getKey());

                                    // Build json response
                                    JSONObject jAccount = new JSONObject();
                                    jAccount.put("id", user.id);
                                    out.println(jAccount.toString());

                                    // Save users list
                                    req.getServletContext().setAttribute("users", users);

                                } else {

                                    // Wrong authToken
                                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    res.setContentType("text/html;charset=UTF-8");
                                    out.println("ERROR 401: Authentication needed!");
                                }
                                found = true;
                                break;
                            }
                        }

                        if (!found) {

                            // User not logged in
                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 404: Not found!");
                        }

                    } else {

                        // Logged in users list empty
                        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 404: Not found!");
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }
            } else if (tokens.length == 3 && (tokens[2].equals("items"))) {
                try {
                    int userId = Integer.parseInt(tokens[1]);

                    // Retrieve list of logged in users
                    HashMap<String, DatabaseTypes.User> users;
                    if ((users = (HashMap<String, DatabaseTypes.User>)req.getServletContext().getAttribute("users")) == null) {

                        // User not logged in
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 401: Authentication needed!");
                    } else {
                        boolean found = false;
                        Iterator it = users.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            DatabaseTypes.User user = (DatabaseTypes.User) entry.getValue();
                            if (user.id == userId) {
                                if (user.authToken.equals(authToken)) {

                                    // Retrieve accounts list
                                    ArrayList<DatabaseTypes.Account> accounts;
                                    if ((accounts = (ArrayList<DatabaseTypes.Account>)req.getServletContext().getAttribute("accounts")) != null && accounts.size() != 0) {
                                        for (DatabaseTypes.Account account : accounts) {
                                            if (account.id == userId) {
                                                if (account.cart.size() == 0) {

                                                    // Users cart is empty
                                                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                                    res.setContentType("text/html;charset=UTF-8");
                                                    out.println("ERROR 404: Not found!");
                                                } else {

                                                    // Build json response
                                                    JSONArray jArray = new JSONArray();
                                                    for (DatabaseTypes.CartItem item : account.cart) {
                                                        JSONObject jCategory = new JSONObject();
                                                        jCategory.put("name", item.name);
                                                        jCategory.put("category", item.category);
                                                        jCategory.put("id", item.id);
                                                        jCategory.put("price", item.price);
                                                        jArray.put(jCategory);
                                                    }

                                                    JSONObject jResponse = new JSONObject();
                                                    jResponse.put("user", userId);
                                                    jResponse.put("items", jArray);
                                                    out.println(jResponse.toString());

                                                    // Clear the cart
                                                    account.cart.clear();

                                                    // Save the accounts list
                                                    req.getServletContext().setAttribute("accounts",accounts);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!found) {

                            // User not logged in
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 401: Authentication needed!");
                        }
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 400: Bad request!");
                }

            } else if (tokens.length == 4 && (tokens[2].equals("items"))) {
                try {
                    int userId = Integer.parseInt(tokens[1]);
                    int itemId = Integer.parseInt(tokens[3]);

                    // Retrieve list of logged in users
                    HashMap<String, DatabaseTypes.User> users;
                    if ((users = (HashMap<String, DatabaseTypes.User>)req.getServletContext().getAttribute("users")) == null) {

                        // User not logged in
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 401: Authentication needed!");
                    } else {
                        boolean found = false;
                        Iterator it = users.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            DatabaseTypes.User user = (DatabaseTypes.User) entry.getValue();
                            if (user.id == userId) {
                                if (user.authToken.equals(authToken)) {

                                    // Retrieve accounts list
                                    ArrayList<DatabaseTypes.Account> accounts;
                                    if ((accounts = (ArrayList<DatabaseTypes.Account>)req.getServletContext().getAttribute("accounts")) != null && accounts.size() != 0) {
                                        for (DatabaseTypes.Account account : accounts) {
                                            if (account.id == userId) {
                                                if (account.cart.size() == 0) {

                                                    // Users cart is empty
                                                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                                    res.setContentType("text/html;charset=UTF-8");
                                                    out.println("ERROR 404: Not found!");
                                                } else {

                                                    // Find the item
                                                    boolean exists = false;
                                                    for (DatabaseTypes.CartItem item : account.cart) {
                                                        if (item.id == itemId) {

                                                            // Build json response
                                                            JSONObject jItem = new JSONObject();
                                                            jItem.put("name", item.name);
                                                            jItem.put("category", item.category);
                                                            jItem.put("id", item.id);
                                                            jItem.put("price", item.price);
                                                            out.println(jItem.toString());

                                                            account.cart.remove(item);

                                                            // Save the accounts list
                                                            req.getServletContext().setAttribute("accounts", accounts);

                                                            exists = true;
                                                            break;
                                                        }
                                                    }

                                                    if (!exists) {

                                                        // Item isn't in the cart
                                                        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                                        res.setContentType("text/html;charset=UTF-8");
                                                        out.println("ERROR 404: Not found!");
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!found) {

                            // User not logged in
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 401: Authentication needed!");
                        }
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
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

            // Can't do DELETE on the logged in list
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            res.setContentType("text/html;charset=UTF-8");
            out.println("ERROR 405: Method not allowed!");
        }
    }
}