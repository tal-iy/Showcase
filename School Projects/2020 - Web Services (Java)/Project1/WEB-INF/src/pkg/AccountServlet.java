package pkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// compile from WEB-INF: javac -cp ../../../lib/servlet-api.jar -d classes src/pkg/*.java
// package from shop: jar -cvf shop.war *
// move war: mv shop.war ../

// login: curl --cookie cookies.txt --cookie-jar cookies.txt -d 'user=User2&pass=UserPass2' 127.0.0.1:2761/shop/account/login
// logout: curl --cookie cookies.txt --cookie-jar cookies.txt -d '' 127.0.0.1:2761/shop/account/logout
// delete: curl --cookie cookies.txt --cookie-jar cookies.txt -d 'user=User3' 127.0.0.1:2761/shop/account/delete

public class AccountServlet extends HttpServlet {

    public static boolean DEBUG = true;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Create a temporary users database
        File usersFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"users.dat");
        if (usersFile.createNewFile()) {

            // Copy all users to the temporary database from the WAR file
            String line;
            FileWriter copyWriter = new FileWriter(usersFile);
            BufferedReader copyReader = new BufferedReader(new InputStreamReader(getServletContext().getResourceAsStream("/WEB-INF/users.dat")));
            while ((line = copyReader.readLine()) != null)
                copyWriter.write(line + "\n");
            copyWriter.close();
            copyReader.close();

        }

        try {
            // Determine which command was sent
            String command = request.getPathInfo();
            if (command.equals("/reset") && DEBUG) {
                usersFile.delete();
            } else if (command.equals("/test") && DEBUG) {
                String line;
                BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(usersFile)));
                while ((line = copyReader.readLine()) != null)
                    out.println(line);
                copyReader.close();
            } else if (command.equals("/login")) {

                // Make sure that the user isn't logged in already
                if (request.getSession(false) == null) {

                    String user = request.getParameter("user");
                    String pass = request.getParameter("pass");

                    // Read the users database until all are read or the user is found
                    String line;
                    BufferedReader usersReader = new BufferedReader(new InputStreamReader(new FileInputStream(usersFile)));
                    while((line = usersReader.readLine()) != null && !user.equals(line))
                        usersReader.readLine();

                    // Make sure a user was found
                    if (user.equals(line)) {

                        // Make sure the passwords match
                        if (pass.equals(usersReader.readLine())) {

                            // Log in the user
                            HttpSession session = request.getSession();
                            session.setAttribute("user",user);
                            out.println("Logged in successfully as "+user+"!");

                        } else out.println("Invalid password!");

                    } else out.println("User doesn't exist!");

                    usersReader.close();

                } else out.println("User already logged in!");

            } else if (command.equals("/logout")) {

                // Make sure that there is a logged in user
                if (request.getSession(false) != null) {

                    // Invalidate the logged in session
                    HttpSession session = request.getSession();
                    session.invalidate();
                    out.println("Logged out successfully!");

                } else out.println("Must be logged in to do that!");

            } else if (command.equals("/register")) {

                String user = request.getParameter("user");
                String pass = request.getParameter("pass");

                // Read the users database until all are read or the user is found
                String line;
                BufferedReader usersReader = new BufferedReader(new InputStreamReader(new FileInputStream(usersFile)));
                while((line = usersReader.readLine()) != null && !line.equals(user))
                    usersReader.readLine();
                usersReader.close();

                // Make sure a user was not found
                if (!user.equals(line)) {

                    // Add a new user to the database file
                    BufferedWriter usersWriter = new BufferedWriter(new FileWriter(usersFile, true));
                    usersWriter.write(user);
                    usersWriter.newLine();
                    usersWriter.write(pass);
                    usersWriter.close();

                    out.println("User "+user+" successfully registered!");

                } else out.println("User "+user+" already exists!");

            } else if (command.equals("/delete")) {

                // Make sure that there is a logged in user
                if (request.getSession(false) != null) {

                    HttpSession session = request.getSession();
                    String loggedUser = (String)session.getAttribute("user");
                    String user = request.getParameter("user");

                    if (loggedUser.equals(user) || loggedUser.equals("Admin")) {

                        // Create a temporary database copy
                        File copyFile = new File((File)getServletContext().getAttribute(ServletContext.TEMPDIR),"users_copy.dat");
                        copyFile.createNewFile();

                        // Copy all users except the deleted one to the new database
                        String line;
                        FileWriter copyWriter = new FileWriter(copyFile);
                        BufferedReader copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(usersFile)));
                        while ((line = copyReader.readLine()) != null) {
                            if (user.equals(line))
                                line = copyReader.readLine(); // Skip the password too
                            else
                                copyWriter.write(line + "\n");
                        }
                        copyWriter.close();
                        copyReader.close();

                        usersFile.delete();
                        copyFile.renameTo(usersFile);

                        // Log out the user
                        session.invalidate();
                        out.println("User "+user+" was deleted!");

                    } else out.println("Wrong user!");

                } else out.println("Must be logged in to do that!");

            } else out.println("Unknown command!");

        } finally {
            out.close();
        }
    }
}