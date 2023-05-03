package app.accounts;

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

public class AccountsController {

    public static Route doPost = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (command.equals("accounts") || command.equals("accounts/")) {

            // Parse json
            JSONObject jsonObject = new JSONObject(req.body());
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");

            // Make sure account doesn't already exist
            if (db.getAccount(username) == null) {

                // Register the new account
                DatabaseTypes.Account account = db.createAccount(username, password);

                // Send json response with userId
                JSONObject jUser = new JSONObject();
                jUser.put("username", username);
                jUser.put("id", account.id);

                res.status(HTTP_CREATED);

                db.close();
                return jUser.toString();

            } else {

                // Account with that username already exists
                res.status(HTTP_CONFLICT);
                res.type("text/html;charset=UTF-8");

                db.close();
                return "ERROR 409: Account with that username already exists!";
            }

        } else {

            // Can't do POST on a specific account
            res.status(HTTP_BAD_METHOD);
            res.type("text/html;charset=UTF-8");

            db.close();
            return "ERROR 405: Method not allowed!";
        }
    };

    public static Route doGet = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (command.equals("accounts") || command.equals("accounts/")) {

            // Retrieve accounts list
            ArrayList<DatabaseTypes.Account> accounts = db.getAccounts();
            if (accounts.size() == 0) {

                // Accounts list empty
                res.status(HTTP_NOT_FOUND);
                res.type("text/html;charset=UTF-8");

                db.close();
                return "ERROR 404: Not found!";

            } else {

                // Build json response
                JSONArray jArray = new JSONArray();
                for (DatabaseTypes.Account account : accounts) {
                    JSONObject jAccount = new JSONObject();
                    jAccount.put("username", account.username);
                    jAccount.put("id", account.id);
                    jArray.put(jAccount);
                }

                JSONObject jResponse = new JSONObject();
                jResponse.put("accounts",jArray);

                db.close();
                return jResponse.toString();
            }

        } else {

            // Can't do GET on a specific account
            res.status(HTTP_BAD_METHOD);
            res.type("text/html;charset=UTF-8");

            db.close();
            return "ERROR 405: Method not allowed!";
        }
    };

    public static Route doPut = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (!command.equals("accounts") && !command.equals("accounts/")) {

            String[] tokens = command.split("/");
            if (tokens.length == 2) {

                // Try to convert the given userId to integer
                try {
                    int userId = Integer.parseInt(tokens[1]);

                    // Parse json
                    JSONObject jsonObject = new JSONObject(req.body());
                    String authToken = jsonObject.getString("authToken");
                    String password = jsonObject.getString("password");

                    // Retrieve account with userId
                    DatabaseTypes.Account account = db.getAccount(userId);
                    if (account != null) {

                        // Make sure the user is logged in and authToken matches
                        if (!account.authToken.equals("noauth") && account.authToken.equals(authToken)) {

                            // Change the password
                            db.updateAccount(userId,"password", password);

                            // Build json response
                            JSONObject jAccount = new JSONObject();
                            jAccount.put("username", account.username);
                            jAccount.put("id", account.id);

                            db.close();
                            return jAccount.toString();

                        } else {

                            // Wrong authToken
                            res.status(HTTP_UNAUTHORIZED);
                            res.type("text/html;charset=UTF-8");

                            db.close();
                            return "ERROR 401: Authentication needed!";
                        }
                    } else {

                        // Account doesn't exist
                        res.status(HTTP_NOT_FOUND);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 404: Not found!";
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

            // Can't do PUT on the accounts list
            res.status(HTTP_BAD_METHOD);
            res.type("text/html;charset=UTF-8");

            db.close();
            return "ERROR 405: Method not allowed!";
        }
    };

    public static Route doDelete = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (!command.equals("accounts") && !command.equals("accounts/")) {

            String[] tokens = command.split("/");
            if (tokens.length == 2) {

                // Try to convert the given userId to integer
                try {
                    int userId = Integer.parseInt(tokens[1]);

                    // Parse json
                    JSONObject jsonObject = new JSONObject(req.body());
                    String authToken = jsonObject.getString("authToken");

                    // Retrieve account with userId
                    DatabaseTypes.Account account = db.getAccount(userId);
                    if (account != null) {

                        // Make sure the user is logged in and authToken matches or admin
                        if (authToken.equals("ADMIN_TOKEN") || (!account.authToken.equals("noauth") && account.authToken.equals(authToken))) {

                            // Remove the account
                            db.deleteAccount(userId);

                            // Build json response
                            JSONObject jAccount = new JSONObject();
                            jAccount.put("username", account.username);
                            jAccount.put("id", account.id);

                            db.close();
                            return jAccount.toString();

                        } else {

                            // Wrong authToken
                            res.status(HTTP_UNAUTHORIZED);
                            res.type("text/html;charset=UTF-8");

                            db.close();
                            return "ERROR 401: Authentication needed!";
                        }
                    } else {

                        // Account doesn't exist
                        res.status(HTTP_NOT_FOUND);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 404: Not found!";
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

            // Can't do DELETE on the accounts list
            res.status(HTTP_BAD_METHOD);
            res.type("text/html;charset=UTF-8");

            db.close();
            return "ERROR 405: Method not allowed!";
        }
    };
}
