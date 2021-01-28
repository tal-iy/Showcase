package app.resources;

import app.api.*;
import app.utils.DatabaseAccess;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;

import static java.net.HttpURLConnection.*;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    public UsersResource() { }

    @GET
    public Account[] getUsers() {

        // Retrieve catalog categories
        DatabaseAccess db = new DatabaseAccess();
        Account[] acts = db.getAuthAccounts();
        db.close();

        // Return accounts list if it isn't empty
        if (acts.length != 0)
            return acts;

        throw new WebApplicationException(HTTP_NOT_FOUND);
    }

    @GET
    @Path("{id}")
    public CartItem[] getCart(@PathParam("id") int userId) {

        // Retrieve account with userId
        DatabaseAccess db = new DatabaseAccess();
        FullAccount account = db.getAccount(userId);
        if (account != null) {

            // Retrieve cart items
            CartItem[] items = db.getCart(userId);
            db.close();
            return items;

        } else {

            // User not logged in
            db.close();
            throw new WebApplicationException(HTTP_UNAUTHORIZED);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public User postUser(UserPost req) {

        // Retrieve account with that username
        DatabaseAccess db = new DatabaseAccess();
        FullAccount account = db.getAccount(req.username);

        if (account != null) {

            // Make sure the passwords match
            if (account.getPassword().equals(req.password)) {

                // Generate a random authToken
                String authToken = new Long(System.nanoTime()).toString();

                // Save authToken to the database
                db.updateAccount(account.getId() ,"authToken", authToken);
                db.close();
                return new User(account.getId(), authToken);

            } else {

                // Wrong password
                db.close();
                throw new WebApplicationException(HTTP_FORBIDDEN);
            }
        } else {

            // Account doesn't exist
            db.close();
            throw new WebApplicationException(HTTP_NOT_FOUND);
        }
    }

    @POST
    @Path("{id}/items")
    @Consumes(MediaType.APPLICATION_JSON)
    public CartItem postItem(@PathParam("id") int userId, ItemPost req) {

        // Retrieve account with that username
        DatabaseAccess db = new DatabaseAccess();
        FullAccount account = db.getAccount(userId);

        if (account != null && account.getAuthToken().equals(req.authToken)) {

            // Retrieve item with that itemId
            CartItem item = db.getItem(req.id);

            if (item != null) {

                // Add new item to the users cart
                item = db.addCart(userId, req.id);
                db.close();
                return item;

            } else {

                // Item doesn't exist
                db.close();
                throw new WebApplicationException(HTTP_NOT_FOUND);
            }
        } else {

            // User not logged in
            db.close();
            throw new WebApplicationException(HTTP_UNAUTHORIZED);
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public User deleteUser(@PathParam("id") int userId, Auth req) {

        // Retrieve account with userId
        DatabaseAccess db = new DatabaseAccess();
        FullAccount account = db.getAccount(userId);
        if (account != null && (req.authToken.equals("ADMIN_TOKEN") || (!account.getAuthToken().equals("noauth") && account.getAuthToken().equals(req.authToken)))) {

            // Remove the authToken from the account
            db.updateAccount(userId, "authToken", "noauth");
            db.close();

            return new User(userId, req.authToken);

        } else {

            // User not logged in
            db.close();
            throw new WebApplicationException(HTTP_UNAUTHORIZED);
        }
    }

    @DELETE
    @Path("{id}/items")
    @Consumes(MediaType.APPLICATION_JSON)
    public CartItem[] deleteCart(@PathParam("id") int userId, Auth req) {

        // Retrieve account with userId
        DatabaseAccess db = new DatabaseAccess();
        FullAccount account = db.getAccount(userId);
        if (account != null && (req.authToken.equals("ADMIN_TOKEN") || (!account.getAuthToken().equals("noauth") && account.getAuthToken().equals(req.authToken)))) {

            // Retrieve cart items
            CartItem[] items = db.getCart(userId);
            db.clearCart(userId);
            db.close();
            return items;

        } else {

            // User not logged in
            db.close();
            throw new WebApplicationException(HTTP_UNAUTHORIZED);
        }
    }

    @DELETE
    @Path("{id}/items/{item}")
    @Consumes(MediaType.APPLICATION_JSON)
    public CartItem deleteItem(@PathParam("id") int userId, @PathParam("item") int itemId, Auth req) {

        // Retrieve account with userId
        DatabaseAccess db = new DatabaseAccess();
        FullAccount account = db.getAccount(userId);
        if (account != null && (req.authToken.equals("ADMIN_TOKEN") || (!account.getAuthToken().equals("noauth") && account.getAuthToken().equals(req.authToken)))) {

            // Retrieve cart item
            CartItem item = db.getCartItem(itemId);
            db.removeCart(itemId);
            db.close();
            return item;

        } else {

            // User not logged in
            db.close();
            throw new WebApplicationException(HTTP_UNAUTHORIZED);
        }
    }

    static class Auth {
        @JsonProperty String authToken;
    }

    static class ItemPost {
        @JsonProperty String authToken;
        @JsonProperty int id;
    }

    static class UserPost {
        @JsonProperty String username;
        @JsonProperty String password;
    }
}