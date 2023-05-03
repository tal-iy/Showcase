package app;

import app.resources.*;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class WebShopApplication extends Application<WebShopConfiguration> {
    public static void main(String[] args) throws Exception {
        new WebShopApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<WebShopConfiguration> bootstrap) { }

    @Override
    public void run(WebShopConfiguration configuration, Environment environment) {

        environment.jersey().register(new CatalogResource());
        environment.jersey().register(new AccountsResource());
        environment.jersey().register(new UsersResource());
    }
}