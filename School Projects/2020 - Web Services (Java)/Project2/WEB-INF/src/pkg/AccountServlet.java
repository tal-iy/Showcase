package pkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import java.util.*;


// compile from WEB-INF: javac -cp ../../../lib/servlet-api.jar:lib/json.jar -d classes src/pkg/*.java
// package from shop: jar -cvf shop.war *
// move war: mv shop.war ../

// login: curl --cookie cookies.txt --cookie-jar cookies.txt -d 'user=User2&pass=UserPass2' 127.0.0.1:2761/shop/accounts
// register: curl --cookie cookies.txt --cookie-jar cookies.txt -d 'user=User2&pass=UserPass2' 127.0.0.1:2761/shop/account/login
// logout: curl --cookie cookies.txt --cookie-jar cookies.txt -d '' 127.0.0.1:2761/shop/account/logout
// delete: curl --cookie cookies.txt --cookie-jar cookies.txt -d 'user=User3' 127.0.0.1:2761/shop/account/delete

// register: curl -d '{"username":"User1", "password":"Pass1"}' -H "Content-Type: application/json" -X POST 127.0.0.1:2761/shop/accounts

public class AccountServlet extends HttpServlet {

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
                    body += line+"\n";
            } catch (IOException ex) { } finally { in.close(); }

            JSONObject jsonObject = new JSONObject(body);

            // Parse json
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");

            // Retrieve accounts list
            Integer nextId;
            if ((nextId = (Integer)req.getServletContext().getAttribute("accounts_nextId")) == null)
                nextId = 0;
            ArrayList<DatabaseTypes.Account> accounts;
            if ((accounts = (ArrayList<DatabaseTypes.Account>)req.getServletContext().getAttribute("accounts")) == null)
                accounts = new ArrayList<>();

            // Look for an account with the same username
            boolean exists = false;
            for (DatabaseTypes.Account account : accounts) {
                if (account.username.equals(username)) {
                    exists = true;
                    break;
                }
            }

            // Make sure account doesn't already exist
            if (exists) {
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                res.setContentType("text/html;charset=UTF-8");
                out.println("ERROR 409: Account with that username already exists!");
            } else {
                // Register the new account
                accounts.add(new DatabaseTypes.Account(username, password, nextId));

                // Send json response with userId
                JSONObject jUser = new JSONObject();
                jUser.put("username", username);
                jUser.put("id", nextId);
                out.println(jUser.toString());

                res.setStatus(HttpServletResponse.SC_CREATED);

                nextId++;
            }

            // Save accounts list
            req.getServletContext().setAttribute("accounts_nextId",nextId);
            req.getServletContext().setAttribute("accounts",accounts);

        } else {

            // Can't do POST on a specific account
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            res.setContentType("text/html;charset=UTF-8");
            out.println("ERROR 405: Method not allowed!");
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();

        String command = req.getPathInfo();
        if (command == null || command.equals("/")) {

            // Retrieve accounts list
            ArrayList<DatabaseTypes.Account> accounts;
            if ((accounts = (ArrayList<DatabaseTypes.Account>)req.getServletContext().getAttribute("accounts")) == null || accounts.size() == 0) {

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

                    // Retrieve accounts list
                    boolean exists = false;
                    ArrayList<DatabaseTypes.Account> accounts;
                    if ((accounts = (ArrayList<DatabaseTypes.Account>)req.getServletContext().getAttribute("accounts")) != null) {

                        // Find an account with the same id
                        DatabaseTypes.Account account = null;
                        for (DatabaseTypes.Account acc : accounts) {
                            if (acc.id == userId) {
                                account = acc;
                                break;
                            }
                        }

                        if (account != null) {

                            // Retrieve logged in list
                            HashMap<String, DatabaseTypes.User> users;
                            if ((users = (HashMap<String, DatabaseTypes.User>)req.getServletContext().getAttribute("users")) != null && users.containsKey(account.username)) {

                                // Make sure the authToken matches
                                DatabaseTypes.User user = users.get(account.username);
                                if (user.authToken.equals(authToken)) {

                                    // Change the password
                                    account.password = password;

                                    // Save accounts list
                                    req.getServletContext().setAttribute("accounts",accounts);

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

                                // Not logged in
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
                    } else {

                        // Accounts list empty
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

                    // Retrieve accounts list
                    boolean exists = false;
                    ArrayList<DatabaseTypes.Account> accounts;
                    if ((accounts = (ArrayList<DatabaseTypes.Account>)req.getServletContext().getAttribute("accounts")) != null) {

                        // Find an account with the same id
                        DatabaseTypes.Account account = null;
                        for (DatabaseTypes.Account acc : accounts) {
                            if (acc.id == userId) {
                                account = acc;
                                break;
                            }
                        }

                        if (account != null) {

                            // Retrieve logged in list
                            HashMap<String, DatabaseTypes.User> users;
                            if ((users = (HashMap<String, DatabaseTypes.User>)req.getServletContext().getAttribute("users")) != null && users.containsKey(account.username)) {

                                // Make sure the authToken matches
                                DatabaseTypes.User user = users.get(account.username);
                                if (authToken.equals("ADMIN_TOKEN") || user.authToken.equals(authToken)) {

                                    // Remove the account
                                    accounts.remove(account);

                                    // Log out the account
                                    users.remove(account.username);

                                    // Save accounts lists
                                    req.getServletContext().setAttribute("accounts",accounts);
                                    req.getServletContext().setAttribute("users",users);

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

                            } else if (authToken.equals("ADMIN_TOKEN")) {

                                // Remove the account
                                accounts.remove(account);

                                // Log out the account if logged in
                                if (users != null)
                                    users.remove(account.username);

                                // Save accounts lists
                                req.getServletContext().setAttribute("accounts",accounts);
                                req.getServletContext().setAttribute("users",users);

                                // Build json response
                                JSONObject jAccount = new JSONObject();
                                jAccount.put("username", account.username);
                                jAccount.put("id", account.id);
                                out.println(jAccount.toString());
                            } else {

                                // Not logged in and not admin
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
                    } else {

                        // Accounts list empty
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
    }
}
