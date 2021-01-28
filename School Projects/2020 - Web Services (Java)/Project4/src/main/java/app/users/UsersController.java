package app.users;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import app.utils.*;
import spark.*;

import static java.net.HttpURLConnection.*;

// list accounts: curl -H "Content-Type: application/json" -X GET 127.0.0.1:2761/shop/accounts
// register: curl -d '{"username":"User1", "password":"Pass1"}' -H "Content-Type: application/json" -X POST 127.0.0.1:2761/shop/accounts
// login: curl -d '{"username":"User1", "password":"Pass1"}' -H "Content-Type: application/json" -X POST 127.0.0.1:2761/shop/users
// logout: curl -d '{"authToken":"XXX"}' -H "Content-Type: application/json" -X DELETE 127.0.0.1:2761/shop/users

public class UsersController{

    public static Route doPost = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (command.equals("users") || command.equals("users/")) {

            // Parse json
            JSONObject jsonObject = new JSONObject(req.body());
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

                    res.status(HTTP_CREATED);

                    db.close();
                    return jUser.toString();

                } else {

                    // Wrong password
                    res.status(HTTP_FORBIDDEN);
                    res.type("text/html;charset=UTF-8");

                    db.close();
                    return "ERROR 403: Forbidden!";
                }
            } else {

                // Account doesn't exist
                res.status(HTTP_NOT_FOUND);
                res.type("text/html;charset=UTF-8");

                db.close();
                return "ERROR 404: Not found!";
            }
        } else {
            String[] tokens = command.split("/");
            if (tokens.length == 3 && tokens[2].equals("items")) {
                try {
                    int userId = Integer.parseInt(tokens[1]);

                    // Parse json
                    JSONObject jsonObject = new JSONObject(req.body());
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

                            db.close();
                            return jItem.toString();

                        } else {
                            // Item doesn't exist
                            res.status(HTTP_NOT_FOUND);
                            res.type("text/html;charset=UTF-8");

                            db.close();
                            return "ERROR 404: Not found!";
                        }
                    } else {

                        // User not logged in
                        res.status(HTTP_UNAUTHORIZED);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 401: Authentication needed!";
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
                    res.status(HTTP_BAD_REQUEST);
                    res.type("text/html;charset=UTF-8");

                    db.close();
                    return "ERROR 400: Bad request!";
                }
            } else {

                // Can't do POST on an item
                res.status(HTTP_BAD_METHOD);
                res.type("text/html;charset=UTF-8");

                db.close();
                return "ERROR 405: Method not allowed!";
            }
        }
    };

    public static Route doGet = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (command.equals("users") || command.equals("users/")) {

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

                db.close();
                return jResponse.toString();
            } else {

                // Logged in users list empty
                res.status(HTTP_NOT_FOUND);
                res.type("text/html;charset=UTF-8");

                db.close();
                return "ERROR 404: Not found!";
            }

        } else {
            String[] tokens = command.split("/");
            if (tokens.length == 3 && tokens[2].equals("items")) {
                try {
                    int userId = Integer.parseInt(tokens[1]);

                    // Parse json
                    JSONObject jsonObject = new JSONObject(req.body());
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

                        db.close();
                        return jResponse.toString();

                    } else {

                        // User not logged in
                        res.status(HTTP_UNAUTHORIZED);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 401: Authentication needed!";
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
                    res.status(HTTP_BAD_REQUEST);
                    res.type("text/html;charset=UTF-8");

                    db.close();
                    return "ERROR 400: Bad request!";
                }
            } else {

                // Too many tokens in the URl
                res.status(HTTP_BAD_REQUEST);
                res.type("text/html;charset=UTF-8");

                db.close();
                return "ERROR 400: Bad request!";
            }
        }
    };

    public static Route doDelete = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (!command.equals("users") && !command.equals("users/")) {

            // Parse json
            JSONObject jsonObject = new JSONObject(req.body());
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

                        // Remove the authToken from the account
                        db.updateAccount(userId, "authToken", "noauth");

                        db.close();
                        return jAccount.toString();
                    } else {

                        // User not logged in
                        res.status(HTTP_UNAUTHORIZED);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 401: Authentication needed!";
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
                    res.status(HTTP_BAD_REQUEST);
                    res.type("text/html;charset=UTF-8");

                    db.close();
                    return "ERROR 400: Bad request!";
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

                        // Clear the cart
                        db.clearCart(userId);

                        db.close();
                        return jResponse.toString();
                    } else {

                        // User not logged in
                        res.status(HTTP_UNAUTHORIZED);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 401: Authentication needed!";
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
                    res.status(HTTP_BAD_REQUEST);
                    res.type("text/html;charset=UTF-8");

                    db.close();
                    return "ERROR 400: Bad request!";
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

                        // Remove the item from the cart
                        db.removeCart(itemId);

                        db.close();
                        return jResponse.toString();
                    } else {

                        // User not logged in
                        res.status(HTTP_UNAUTHORIZED);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 401: Authentication needed!";
                    }
                } catch (NumberFormatException ex) {

                    // userId is supposed to be an integer value
                    res.status(HTTP_BAD_REQUEST);
                    res.type("text/html;charset=UTF-8");

                    db.close();
                    return "ERROR 400: Bad request!";
                }
            } else {

                // Too many tokens in the URl
                res.status(HTTP_BAD_REQUEST);
                res.type("text/html;charset=UTF-8");

                db.close();
                return "ERROR 400: Bad request!";
            }

        } else {

            // Can't do DELETE on the logged in list
            res.status(HTTP_BAD_METHOD);
            res.type("text/html;charset=UTF-8");

            db.close();
            return "ERROR 405: Method not allowed!";
        }
    };
}