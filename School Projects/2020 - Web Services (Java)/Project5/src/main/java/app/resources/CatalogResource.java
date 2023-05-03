package app.resources;

import app.api.*;
import app.utils.DatabaseAccess;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static java.net.HttpURLConnection.*;

@Path("catalogs")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogResource {

    public CatalogResource() { }

    @GET
    public Catalog[] getCatalogs() {

        // Retrieve catalog categories
        DatabaseAccess db = new DatabaseAccess();
        Catalog[] cat = db.getCategories();
        db.close();

        // Return categories list if it isn't empty
        if (cat.length != 0)
            return cat;

        throw new WebApplicationException(HTTP_NOT_FOUND);
    }

    @GET
    @Path("{id}/items")
    public FullCatalog getItems(@PathParam("id") int categoryId) {

        // Retrieve the category with its items
        DatabaseAccess db = new DatabaseAccess();
        FullCatalog cat = db.getCategory(categoryId);
        db.close();

        return cat;
    }

    @GET
    @Path("{id}/items/{item}")
    public CartItem getItem(@PathParam("id") int categoryId, @PathParam("item") int itemId) {

        // Retrieve the category with its items
        DatabaseAccess db = new DatabaseAccess();
        FullCatalog cat = db.getCategory(categoryId);
        db.close();

        // Make sure the category exists
        if (cat != null) {
            // Search through the categories items
            for (CartItem item : cat.getItems())
                if (item.getId() == itemId)
                    return item;
        }

        throw new WebApplicationException(HTTP_NOT_FOUND);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Catalog postCatalog(CatalogPost req) {
        DatabaseAccess db = new DatabaseAccess();

        // Make sure it's the admin
        if (req.authToken.equals("ADMIN_TOKEN")) {

            // Create a new category
            Catalog cat = db.createCategory(req.name);
            db.close();
            return cat;
        } else {
            // Not admin
            db.close();
            throw new WebApplicationException(HTTP_FORBIDDEN);
        }
    }

    @POST
    @Path("{id}/items")
    @Consumes(MediaType.APPLICATION_JSON)
    public CartItem postItem(@PathParam("id") int categoryId, ItemPost req) {
        DatabaseAccess db = new DatabaseAccess();

        // Make sure it's the admin
        if (req.authToken.equals("ADMIN_TOKEN")) {
            // Make sure the category exists
            if (db.getCategory(categoryId) != null) {

                // Add new item to the database
                CartItem item = db.createItem(categoryId, req.name, req.price, req.description);
                db.close();
                return item;

            } else {

                // Category doesn't exist
                db.close();
                throw new WebApplicationException(HTTP_NOT_FOUND);
            }
        } else {

            // Not admin
            db.close();
            throw new WebApplicationException(HTTP_FORBIDDEN);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Catalog putCatalog(@PathParam("id") int categoryId, CatalogPost req) {
        DatabaseAccess db = new DatabaseAccess();

        // Make sure it's the admin
        if (req.authToken.equals("ADMIN_TOKEN")) {

            // Make sure category exists
            if (db.getCategory(categoryId) != null) {

                // Update the database with the new name
                db.updateCatagory(categoryId, "name", req.name);
                db.close();
                return new Catalog(categoryId, req.name);

            } else {

                // Category doesn't exist
                db.close();
                throw new WebApplicationException(HTTP_NOT_FOUND);
            }
        } else {

            // Not admin
            db.close();
            throw new WebApplicationException(HTTP_FORBIDDEN);
        }
    }

    @PUT
    @Path("{id}/items/{item}")
    @Consumes(MediaType.APPLICATION_JSON)
    public CartItem putItem(@PathParam("id") int categoryId, @PathParam("item") int itemId, ItemPost req) {
        DatabaseAccess db = new DatabaseAccess();

        // Make sure it's the admin
        if (req.authToken.equals("ADMIN_TOKEN")) {

            // Make sure category exists
            if (db.getCategory(categoryId) != null) {

                // Make sure the item is in the category
                CartItem item = db.getItem(itemId);
                if (item.getCategory() == categoryId) {

                    // Update the database with new item info
                    db.updateItem(itemId, req.name, req.price, req.description);
                    db.close();
                    return new CartItem(itemId, categoryId, req.name, req.price, req.description);

                } else {

                    // Item doesn't belong in category
                    db.close();
                    throw new WebApplicationException(HTTP_NOT_FOUND);
                }

            } else {

                // Category doesn't exist
                db.close();
                throw new WebApplicationException(HTTP_NOT_FOUND);
            }
        } else {

            // Not admin
            db.close();
            throw new WebApplicationException(HTTP_FORBIDDEN);
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Catalog deleteCatalog(@PathParam("id") int categoryId, Auth req) {
        DatabaseAccess db = new DatabaseAccess();

        // Make sure it's the admin
        if (req.authToken.equals("ADMIN_TOKEN")) {

            // Retrieve the category
            FullCatalog cat = db.getCategory(categoryId);

            // Make sure the category exists
            if (cat != null) {

                // Delete the category from the database
                db.deleteCategory(categoryId);
                db.close();
                return new Catalog(categoryId, cat.getName());

            } else {

                // Category list empty
                db.close();
                throw new WebApplicationException(HTTP_NOT_FOUND);
            }
        } else {

            // Not admin
            db.close();
            throw new WebApplicationException(HTTP_FORBIDDEN);
        }
    }

    @DELETE
    @Path("{id}/items/{item}")
    @Consumes(MediaType.APPLICATION_JSON)
    public CartItem deleteItem(@PathParam("id") int categoryId, @PathParam("item") int itemId, Auth req) {
        DatabaseAccess db = new DatabaseAccess();

        // Make sure it's the admin
        if (req.authToken.equals("ADMIN_TOKEN")) {

            // Make sure category exists
            if (db.getCategory(categoryId) != null) {

                // Make sure the item is in the category
                CartItem item = db.getItem(itemId);
                if (item.getCategory() == categoryId) {

                    // Delete the item from the database
                    db.deleteItem(itemId);
                    db.close();
                    return item;

                } else {

                    // Item doesn't belong in category
                    db.close();
                    throw new WebApplicationException(HTTP_NOT_FOUND);
                }
            } else {

                // Category doesn't exist
                db.close();
                throw new WebApplicationException(HTTP_NOT_FOUND);
            }
        } else {

            // Not admin
            db.close();
            throw new WebApplicationException(HTTP_FORBIDDEN);
        }
    }

    static class Auth {
        @JsonProperty String authToken;
    }

    static class CatalogPost {
        @JsonProperty String authToken;
        @JsonProperty String name;
    }

    static class ItemPost {
        @JsonProperty String authToken;
        @JsonProperty String name;
        @JsonProperty double price;
        @JsonProperty String description;
    }
}