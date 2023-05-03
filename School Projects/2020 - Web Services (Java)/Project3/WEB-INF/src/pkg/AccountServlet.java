package pkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import java.util.*;



// compile from WEB-INF: javac -cp ../../../lib/servlet-api.jar:lib/json.jar:lib/mysql.jar -d classes src/pkg/*.java
// package from shop: jar -cvf shop.war *
// move war: mv shop.war ../

// list accounts: curl -H "Content-Type: application/json" -X GET 127.0.0.1:2761/shop/accounts
// register: curl -d '{"username":"User1", "password":"Pass1"}' -H "Content-Type: application/json" -X POST 127.0.0.1:2761/shop/accounts
// login: curl -d '{"username":"User1", "password":"Pass1"}' -H "Content-Type: application/json" -X POST 127.0.0.1:2761/shop/users
// logout: curl -d '{"authToken":"XXX"}' -H "Content-Type: application/json" -X DELETE 127.0.0.1:2761/shop/users

public class AccountServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

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
                    body += line+"\n";
            } catch (IOException ex) { } finally { in.close(); }

            JSONObject jsonObject = new JSONObject(body);

            // Parse json
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

                res.setStatus(HttpServletResponse.SC_CREATED);
                out.println(jUser.toString());

            } else {

                // Account with that username already exists
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 409: Account with that username already exists!");
            }

        } else {

            // Can't do POST on a specific account
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            res.setContentType("text/html;charset=UTF-8");
            out.println("ERROR 405: Method not allowed!");
        }

        db.close();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        DatabaseAccess db = new DatabaseAccess();

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();

        String command = req.getPathInfo();
        if (command == null || command.equals("/")) {

            // Retrieve accounts list
            ArrayList<DatabaseTypes.Account> accounts = db.getAccounts();
            if (accounts.size() == 0) {

                // Accounts list empty
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 404: Not found!");

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
                out.println(jResponse.toString());
            }

        } else {

            // Can't do GET on a specific account
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            res.setContentType("text/html;charset=UTF-8");
            out.println("ERROR 405: Method not allowed!");
        }

        db.close();
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        DatabaseAccess db = new DatabaseAccess();

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        BufferedReader in = req.getReader();

        String command = req.getPathInfo();
        if (command != null && !command.equals("/")) {

            String[] tokens = command.split("/");
            if (tokens.length == 2) {

                // Try to convert the given userId to integer
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

                    JSONObject jsonObject = new JSONObject(body);

                    // Parse json
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
                            out.println(jAccount.toString());

                        } else {

                            // Wrong authToken
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 401: Authentication needed!");
                        }
                    } else {

                        // Account doesn't exist
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
            } else {

                // Too many tokens in the URl
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 400: Bad request!");
            }
        } else {

            // Can't do PUT on the accounts list
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            res.setContentType("text/html;charset=UTF-8");
            out.println("ERROR 405: Method not allowed!");
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

            String[] tokens = command.split("/");
            if (tokens.length == 2) {

                // Try to convert the given userId to integer
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

                    JSONObject jsonObject = new JSONObject(body);

                    // Parse json
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
                            out.println(jAccount.toString());

                        } else {

                            // Wrong authToken
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("text/html;charset=UTF-8");
                            out.println("ERROR 401: Authentication needed!");
                        }
                    } else {

                        // Account doesn't exist
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
            } else {

                // Too many tokens in the URl
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 400: Bad request!");
            }
        } else {

            // Can't do DELETE on the accounts list
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            res.setContentType("text/html;charset=UTF-8");
            out.println("ERROR 405: Method not allowed!");
        }

        db.close();
    }
}
