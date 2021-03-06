package app.catalog;

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

public class CatalogController{

    public static Route doPost = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (command.equals("catalogs") || command.equals("catalogs/")) {

            // Parse json
            JSONObject jsonObject = new JSONObject(req.body());
            String authToken = jsonObject.getString("authToken");
            String name = jsonObject.getString("name");

            // Make sure it's the admin
            if (authToken.equals("ADMIN_TOKEN")) {

                // Create a new category
                DatabaseTypes.CatalogCategory cat = db.createCategory(name);

                // Send json response with category ID and name
                JSONObject jCategory = new JSONObject();
                jCategory.put("id", cat.id);
                jCategory.put("name", name);

                res.status(HTTP_CREATED);

                db.close();
                return jCategory.toString();
            } else {

                // Not admin
                res.status(HTTP_FORBIDDEN);
                res.type("text/html;charset=UTF-8");

                db.close();
                return "ERROR 403: Forbidden!";
            }
        } else {
            String[] tokens = command.split("/");
            if (tokens.length == 3 && tokens[2].equals("items")) {

                // Try to convert the categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);

                    // Parse json
                    JSONObject jsonObject = new JSONObject(req.body());
                    String authToken = jsonObject.getString("authToken");
                    String name = jsonObject.getString("name");
                    double price = jsonObject.getDouble("price");
                    String descr = jsonObject.getString("desc");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {
                        // Make sure the category exists
                        if (db.getCategory(categoryId) != null) {

                            // Add new item to the database
                            DatabaseTypes.CartItem item = db.createItem(categoryId, name, price, descr);

                            // Send json response
                            JSONObject jItem = new JSONObject();
                            jItem.put("name", item.name);
                            jItem.put("id", item.id);
                            jItem.put("category", item.category);
                            jItem.put("price", item.price);
                            jItem.put("desc", item.description);

                            res.status(HTTP_CREATED);

                            db.close();
                            return jItem.toString();

                        } else {
                            // Category doesn't exist
                            res.status(HTTP_NOT_FOUND);
                            res.type("text/html;charset=UTF-8");

                            db.close();
                            return "ERROR 404: Not found!";
                        }
                    } else {

                        // Not admin
                        res.status(HTTP_FORBIDDEN);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 403: Forbidden!";
                    }
                } catch (NumberFormatException ex) {

                    // categoryId is supposed to be an integer value
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

    public static Route doGet = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (command.equals("catalogs") || command.equals("catalogs/")) {

            // Retrieve catalog list
            ArrayList<DatabaseTypes.CatalogCategory> categories = db.getCategories();

            if (categories.size() != 0) {

                // Build json response
                JSONArray jArray = new JSONArray();
                for (DatabaseTypes.CatalogCategory category : categories) {
                    JSONObject jCategory = new JSONObject();
                    jCategory.put("name", category.name);
                    jCategory.put("id", category.id);
                    jArray.put(jCategory);
                }

                JSONObject jResponse = new JSONObject();
                jResponse.put("categories",jArray);

                db.close();
                return jResponse.toString();

            } else {

                // Category list empty
                res.status(HTTP_NOT_FOUND);
                res.type("text/html;charset=UTF-8");

                db.close();
                return "ERROR 404: Not found!";
            }
        } else {
            String[] tokens = command.split("/");
            if (tokens.length == 3 && tokens[2].equals("items")) {

                // Try to convert the categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);

                    // Retrieve the category with its items
                    DatabaseTypes.CatalogCategory cat = db.getCategory(categoryId);

                    // Make sure the category exists
                    if (cat != null) {
                        // Build json response
                        JSONArray jArray = new JSONArray();
                        for (DatabaseTypes.CartItem item : cat.cart) {
                            JSONObject jItem = new JSONObject();
                            jItem.put("id", item.id);
                            jItem.put("name", item.name);
                            jItem.put("price", item.price);
                            jArray.put(jItem);
                        }

                        JSONObject jResponse = new JSONObject();
                        jResponse.put("id", cat.id);
                        jResponse.put("category", cat.name);
                        jResponse.put("items", jArray);

                        db.close();
                        return jResponse.toString();
                    } else {

                        // Category doesn't exist
                        res.status(HTTP_NOT_FOUND);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 404: Not found!";
                    }
                } catch (NumberFormatException ex) {

                    // categoryId is supposed to be an integer value
                    res.status(HTTP_BAD_REQUEST);
                    res.type("text/html;charset=UTF-8");

                    db.close();
                    return "ERROR 400: Bad request!";
                }

            } else if (tokens.length == 4 && tokens[2].equals("items")) {

                // Try to convert the itemId and categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);
                    int itemId = Integer.parseInt(tokens[3]);

                    // Retrieve the category with its items
                    DatabaseTypes.CatalogCategory cat = db.getCategory(categoryId);

                    // Make sure the category exists
                    if (cat != null) {

                        // Search through the categories items
                        boolean found = false;
                        for (DatabaseTypes.CartItem item : cat.cart) {
                            if (item.id == itemId) {

                                // Build jason response
                                JSONObject jItem = new JSONObject();
                                jItem.put("id", item.id);
                                jItem.put("category", categoryId);
                                jItem.put("name", item.name);
                                jItem.put("price", item.price);
                                jItem.put("desc", item.description);

                                db.close();
                                return jItem.toString();
                            }
                        }

                        // Item doesn't exist
                        res.status(HTTP_NOT_FOUND);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 404: Not found!";
                    } else {

                        // Category doesn't exist
                        res.status(HTTP_NOT_FOUND);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 404: Not found!";
                    }
                } catch (NumberFormatException ex) {

                    // itemId and categoryId are supposed to be integer values
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

    public static Route doPut = (Request req, Response res) -> {

        DatabaseAccess db = new DatabaseAccess();

        res.type("application/json;charset=UTF-8");

        String command = req.pathInfo().substring(1);
        if (!command.equals("catalogs") && !command.equals("catalogs/")) {

            String[] tokens = command.split("/");
            if (tokens.length == 2) {

                // Try to convert the categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);

                    // Parse json
                    JSONObject jsonObject = new JSONObject(req.body());
                    String authToken = jsonObject.getString("authToken");
                    String name = jsonObject.getString("name");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {

                        // Make sure category exists
                        if (db.getCategory(categoryId) != null) {

                            // Update the database with the new name
                            db.updateCatagory(categoryId, "name", name);

                            // Build json response
                            JSONObject jCategory = new JSONObject();
                            jCategory.put("id", categoryId);
                            jCategory.put("name", name);

                            db.close();
                            return jCategory.toString();

                        } else {

                            // Category doesn't exist
                            res.status(HTTP_NOT_FOUND);
                            res.type("text/html;charset=UTF-8");

                            db.close();
                            return "ERROR 404: Not found!";
                        }
                    } else {

                        // Not admin
                        res.status(HTTP_FORBIDDEN);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 403: Forbidden!";
                    }

                } catch (NumberFormatException ex) {

                    // categoryId is supposed to be an integer value
                    res.status(HTTP_BAD_REQUEST);
                    res.type("text/html;charset=UTF-8");

                    db.close();
                    return "ERROR 400: Bad request!";
                }

            } else if (tokens.length == 4 && tokens[2].equals("items")) {

                // Try to convert the categoryId to an integer
                try {

                    int categoryId = Integer.parseInt(tokens[1]);
                    int itemId = Integer.parseInt(tokens[3]);

                    // Parse json
                    JSONObject jsonObject = new JSONObject(req.body());
                    String authToken = jsonObject.getString("authToken");
                    String name = jsonObject.getString("name");
                    double price = jsonObject.getDouble("price");
                    String descr = jsonObject.getString("desc");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {

                        // Make sure category exists
                        if (db.getCategory(categoryId) != null) {

                            // Make sure the item is in the category
                            DatabaseTypes.CartItem item = db.getItem(itemId);
                            if (item.category == categoryId) {

                                // Update the database with new item info
                                db.updateItem(itemId, name, price, descr);

                                // Build json response
                                JSONObject jItem = new JSONObject();
                                jItem.put("id", itemId);
                                jItem.put("category", categoryId);
                                jItem.put("name", name);
                                jItem.put("price", price);
                                jItem.put("desc", descr);

                                db.close();
                                return jItem.toString();

                            } else {

                                // Item doesn't belong in category
                                res.status(HTTP_NOT_FOUND);
                                res.type("text/html;charset=UTF-8");

                                db.close();
                                return "ERROR 404: Not found!";
                            }

                        } else {

                            // Category doesn't exist
                            res.status(HTTP_NOT_FOUND);
                            res.type("text/html;charset=UTF-8");

                            db.close();
                            return "ERROR 404: Not found!";
                        }
                    } else {

                        // Not admin
                        res.status(HTTP_FORBIDDEN);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 403: Forbidden!";
                    }

                } catch (NumberFormatException ex) {

                    // itemId and categoryId are supposed to be integer values
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

            // Can't do PUT on the category list
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
        if (!command.equals("catalogs") && !command.equals("catalogs/")) {

            String[] tokens = command.split("/");
            if (tokens.length == 2) {

                // Try to convert the categoryId to an integer
                try {
                    int categoryId = Integer.parseInt(tokens[1]);

                    // Parse json
                    JSONObject jsonObject = new JSONObject(req.body());
                    String authToken = jsonObject.getString("authToken");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {

                        // Retrieve the category
                        DatabaseTypes.CatalogCategory cat = db.getCategory(categoryId);

                        // Make sure the category exists
                        if (cat != null) {

                            // Build json response
                            JSONObject jCategory = new JSONObject();
                            jCategory.put("name", cat.name);
                            jCategory.put("id", cat.id);

                            // Delete the category from the database
                            db.deleteCategory(categoryId);

                            db.close();
                            return jCategory.toString();
                        } else {

                            // Category list empty
                            res.status(HTTP_NOT_FOUND);
                            res.type("text/html;charset=UTF-8");

                            db.close();
                            return "ERROR 404: Not found!";
                        }
                    } else {

                        // Not admin
                        res.status(HTTP_FORBIDDEN);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 403: Forbidden!";
                    }

                } catch (NumberFormatException ex) {

                    // categoryId is supposed to be an integer value
                    res.status(HTTP_BAD_REQUEST);
                    res.type("text/html;charset=UTF-8");

                    db.close();
                    return "ERROR 400: Bad request!";
                }

            } else if (tokens.length == 4 && tokens[2].equals("items")) {

                // Try to convert the categoryId to an integer
                try {

                    int categoryId = Integer.parseInt(tokens[1]);
                    int itemId = Integer.parseInt(tokens[3]);

                    // Parse json
                    JSONObject jsonObject = new JSONObject(req.body());
                    String authToken = jsonObject.getString("authToken");

                    // Make sure it's the admin
                    if (authToken.equals("ADMIN_TOKEN")) {

                        // Make sure category exists
                        if (db.getCategory(categoryId) != null) {

                            // Make sure the item is in the category
                            DatabaseTypes.CartItem item = db.getItem(itemId);
                            if (item.category == categoryId) {

                                // Build json response
                                JSONObject jItem = new JSONObject();
                                jItem.put("id", itemId);
                                jItem.put("category", categoryId);
                                jItem.put("name", item.name);
                                jItem.put("price", item.price);
                                jItem.put("desc", item.description);

                                // Delete the item from the database
                                db.deleteItem(itemId);

                                db.close();
                                return jItem.toString();
                            } else {

                                // Item doesn't belong in category
                                res.status(HTTP_NOT_FOUND);
                                res.type("text/html;charset=UTF-8");

                                db.close();
                                return "ERROR 404: Not found!";
                            }
                        } else {

                            // Category doesn't exist
                            res.status(HTTP_NOT_FOUND);
                            res.type("text/html;charset=UTF-8");

                            db.close();
                            return "ERROR 404: Not found!";
                        }
                    } else {

                        // Not admin
                        res.status(HTTP_FORBIDDEN);
                        res.type("text/html;charset=UTF-8");

                        db.close();
                        return "ERROR 403: Forbidden!";
                    }

                } catch (NumberFormatException ex) {

                    // itemId and categoryId are supposed to be integer values
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

            // Can't do DELETE on the category list
            res.status(HTTP_BAD_METHOD);
            res.type("text/html;charset=UTF-8");

            db.close();
            return "ERROR 405: Method not allowed!";
        }
    };
}
