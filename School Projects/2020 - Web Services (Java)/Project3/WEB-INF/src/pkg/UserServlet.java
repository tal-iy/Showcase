package pkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.crypto.Data;

import org.json.*;
import java.util.*;

// compile from WEB-INF: javac -cp ../../../lib/servlet-api.jar:lib/json.jar:lib/mysql.jar -d classes src/pkg/*.java
// package from shop: jar -cvf shop.war *
// move war: mv shop.war ../

// list accounts: curl -H "Content-Type: application/json" -X GET 127.0.0.1:2761/shop/accounts
// register: curl -d '{"username":"User1", "password":"Pass1"}' -H "Content-Type: application/json" -X POST 127.0.0.1:2761/shop/accounts
// login: curl -d '{"username":"User1", "password":"Pass1"}' -H "Content-Type: application/json" -X POST 127.0.0.1:2761/shop/users
// logout: curl -d '{"authToken":"XXX"}' -H "Content-Type: application/json" -X DELETE 127.0.0.1:2761/shop/users

public class UserServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        DatabaseAccess db = new DatabaseAccess();

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

            // Retrieve account with that username
            DatabaseTypes.Account account = db.getAccount(username);
            if (account != null) {

                // Make sure the passwords match
                if (account.password.equals(password)) {

                    // Generate a random authToken
                    String authToken = new Long(System.nanoTime()).toString();

                    // Save authToken to the database
                    db.updateAccount(account.id,"authToken", authToken);

                    // Send json response with userId and authToken
                    JSONObject jUser = new JSONObject();
                    jUser.put("authToken", authToken);
                    jUser.put("id", account.id);

                    res.setStatus(HttpServletResponse.SC_CREATED);
                    out.println(jUser.toString());

                } else {

                    // Wrong password
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.setContentType("text/html;charset=UTF-8");
                    out.println("ERROR 403: Forbidden!");
                }
            } else {

                // Account doesn't exist
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
                    int itemId = jsonObject.getInt("id");

                    // Retrieve account with that username
                    DatabaseTypes.Account account = db.getAccount(userId);
                    if (account != null && account.authToken.equals(authToken)) {

                        // Retrieve item with that itemId
                        DatabaseTypes.CartItem item = db.getItem(itemId);

                        if (item != null) {

                            // Add new item to the users cart
                            item = db.addCart(userId, itemId);

                            // Build json response
                            JSONObject jItem = new JSONObject();
                            jItem.put("user", userId);
                            jItem.put("id", item.id);
                            jItem.put("category", item.category);
                            jItem.put("price", item.price);
                            jItem.put("desc", item.description);
                            out.println(jItem.toString());

                        } else {
                            // Item doesn't exist
                            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 404: Not found!");
                        }
                    } else {

                        // User not logged in
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 401: Authentication needed!");
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

        db.close();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        DatabaseAccess db = new DatabaseAccess();

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        BufferedReader in = req.getReader();

        String command = req.getPathInfo();
        if (command == null || command.equals("/")) {

            ArrayList<DatabaseTypes.Account> accounts = db.getAuthAccounts();
            if (accounts.size() != 0) {

                // Build json response
                JSONArray jUsers = new JSONArray();
                for (DatabaseTypes.Account account : accounts) {
                    JSONObject jUser = new JSONObject();
                    jUser.put("username", account.username);
                    jUser.put("id", account.id);
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

                    // Retrieve account with userId
                    DatabaseTypes.Account account = db.getAccount(userId);
                    if (account != null && account.authToken.equals(authToken)) {

                        // Retrieve cart items
                        ArrayList<DatabaseTypes.CartItem> items = db.getCart(userId);

                        // Build json response
                        JSONArray jArray = new JSONArray();
                        for (DatabaseTypes.CartItem item : items) {
                            JSONObject jItem = new JSONObject();
                            jItem.put("id", item.id);
                            jItem.put("category", item.category);
                            jItem.put("name", item.name);
                            jItem.put("price", item.price);
                            jArray.put(jItem);
                        }

                        JSONObject jResponse = new JSONObject();
                        jResponse.put("user", userId);
                        jResponse.put("items", jArray);
                        out.println(jResponse.toString());

                    } else {

                        // User not logged in
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 401: Authentication needed!");
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

        db.close();
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        DatabaseAccess db = new DatabaseAccess();

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

                    // Retrieve account with userId
                    DatabaseTypes.Account account = db.getAccount(userId);
                    if (account != null && (authToken.equals("ADMIN_TOKEN") || (!account.authToken.equals("noauth") && account.authToken.equals(authToken)))) {

                        // Build json response
                        JSONObject jAccount = new JSONObject();
                        jAccount.put("id", userId);
                        out.println(jAccount.toString());

                        // Remove the authToken from the account
                        db.updateAccount(userId, "authToken", "noauth");

                    } else {

                        // User not logged in
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 401: Authentication needed!");
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

                    // Retrieve account with userId
                    DatabaseTypes.Account account = db.getAccount(userId);
                    if (account != null && (authToken.equals("ADMIN_TOKEN") || (!account.authToken.equals("noauth") && account.authToken.equals(authToken)))) {

                        // Retrieve cart items
                        ArrayList<DatabaseTypes.CartItem> items = db.getCart(userId);

                        // Build json response
                        JSONArray jArray = new JSONArray();
                        for (DatabaseTypes.CartItem item : items) {
                            JSONObject jItem = new JSONObject();
                            jItem.put("id", item.id);
                            jItem.put("category", item.category);
                            jItem.put("name", item.name);
                            jItem.put("price", item.price);
                            jArray.put(jItem);
                        }

                        JSONObject jResponse = new JSONObject();
                        jResponse.put("user", userId);
                        jResponse.put("items", jArray);
                        out.println(jResponse.toString());

                        // Clear the cart
                        db.clearCart(userId);

                    } else {

                        // User not logged in
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 401: Authentication needed!");
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

                    // Retrieve account with userId
                    DatabaseTypes.Account account = db.getAccount(userId);
                    if (account != null && (authToken.equals("ADMIN_TOKEN") || (!account.authToken.equals("noauth") && account.authToken.equals(authToken)))) {

                        // Retrieve cart item
                        DatabaseTypes.CartItem item = db.getCartItem(itemId);

                        // Build json response
                        JSONObject jResponse = new JSONObject();
                        jResponse.put("id", item.id);
                        jResponse.put("category", item.category);
                        jResponse.put("name", item.name);
                        jResponse.put("price", item.price);
                        out.println(jResponse.toString());

                        // Remove the item from the cart
                        db.removeCart(itemId);

                    } else {

                        // User not logged in
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("text/html;charset=UTF-8");
                        out.println("ERROR 401: Authentication needed!");
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

        db.close();
    }
}