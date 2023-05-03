package app;

import app.accounts.*;
import app.catalog.*;
import app.users.*;
import org.apache.log4j.BasicConfigurator;

import static spark.Spark.*;

// Example curl commands:
//
// list accounts: curl -H "Content-Type: application/json" -X GET 127.0.0.1:2761/shop/accounts
// register: curl -d '{"username":"User5", "password":"Pass1"}' -H "Content-Type: application/json" -X POST 127.0.0.1:2761/shop/accounts
// login: curl -d '{"username":"User5", "password":"Pass1"}' -H "Content-Type: application/json" -X POST 127.0.0.1:2761/shop/users
// logout: curl -d '{"authToken":"XXX"}' -H "Content-Type: application/json" -X DELETE 127.0.0.1:2761/shop/users
//

public class WebShop {

    public static void main(String args[]) {

        BasicConfigurator.configure();

        port(2761);

        post("/accounts", AccountsController.doPost);
        post("/users", UsersController.doPost);
        post("/catalogs", CatalogController.doPost);

        get("/accounts", AccountsController.doGet);
        get("/users", UsersController.doGet);
        get("/catalogs", CatalogController.doGet);

        put("/accounts", AccountsController.doPut);
        put("/catalogs", CatalogController.doPut);

        delete("/accounts", AccountsController.doDelete);
        delete("/users", UsersController.doDelete);
        delete("/catalogs", CatalogController.doDelete);

        post("/accounts/*", AccountsController.doPost);
        post("/users/*", UsersController.doPost);
        post("/catalogs/*", CatalogController.doPost);

        get("/accounts/*", AccountsController.doGet);
        get("/users/*", UsersController.doGet);
        get("/catalogs/*", CatalogController.doGet);

        put("/accounts/*", AccountsController.doPut);
        put("/catalogs/*", CatalogController.doPut);

        delete("/accounts/*", AccountsController.doDelete);
        delete("/users/*", UsersController.doDelete);
        delete("/catalogs/*", CatalogController.doDelete);

    }
}
