package app.resources;

import app.api.*;
import app.utils.DatabaseAccess;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static java.net.HttpURLConnection.*;

@Path("accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountsResource {

    public AccountsResource() { }

    @GET
    public Account[] getAccounts() {

        // Retrieve catalog categories
        DatabaseAccess db = new DatabaseAccess();
        Account[] acts = db.getAccounts();
        db.close();

        // Return accounts list if it isn't empty
        if (acts.length != 0)
            return acts;

        throw new WebApplicationException(HTTP_NOT_FOUND);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public FullAccount postAccount(AccountPost req) {

        DatabaseAccess db = new DatabaseAccess();

        // Make sure account doesn't already exist
        if (db.getAccount(req.username) == null) {

            // Register the new account
            FullAccount account = db.createAccount(req.username, req.password);
            db.close();
            return account;

        } else {

            // Account with that username already exists
            db.close();
            throw new WebApplicationException(HTTP_CONFLICT);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Account putAccount(@PathParam("id") int userId, AccountPut req) {

        DatabaseAccess db = new DatabaseAccess();

        // Retrieve account with userId
        FullAccount account = db.getAccount(userId);
        if (account != null) {

            // Make sure the user is logged in and authToken matches
            if (!account.getAuthToken().equals("noauth") && account.getAuthToken().equals(req.authToken)) {

                // Change the password
                db.updateAccount(userId,"password", req.password);
                db.close();
                return new Account(userId, account.getUsername());

            } else {

                // Wrong authToken
                db.close();
                throw new WebApplicationException(HTTP_UNAUTHORIZED);
            }
        } else {

            // Account doesn't exist
            db.close();
            throw new WebApplicationException(HTTP_NOT_FOUND);
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Account deleteAccount(@PathParam("id") int userId, Auth req) {

        DatabaseAccess db = new DatabaseAccess();

        // Retrieve account with userId
        FullAccount account = db.getAccount(userId);
        if (account != null) {

            // Make sure the user is logged in and authToken matches or admin
            if (req.authToken.equals("ADMIN_TOKEN") || (!account.getAuthToken().equals("noauth") && account.getAuthToken().equals(req.authToken))) {

                // Remove the account
                db.deleteAccount(userId);
                db.close();
                return new Account(userId, account.getUsername());

            } else {

                // Wrong authToken
                db.close();
                throw new WebApplicationException(HTTP_UNAUTHORIZED);
            }
        } else {

            // Account doesn't exist
            db.close();
            throw new WebApplicationException(HTTP_NOT_FOUND);
        }
    }

    static class Auth {
        @JsonProperty String authToken;
    }

    static class AccountPost {
        @JsonProperty String username;
        @JsonProperty String password;
    }

    static class AccountPut {
        @JsonProperty String authToken;
        @JsonProperty String password;
    }
}