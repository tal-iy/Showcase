package pkg;
import java.util.ArrayList;

public class DatabaseTypes {

    public static class Account {
        public String username;
        public String password;
        public String authToken;
        public int id;
        public ArrayList<CartItem> cart;

        public Account(int id, String username, String password, String authToken) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.authToken = authToken;
            cart = new ArrayList<>();
        }
    }

    public static class CatalogCategory {
        public int id;
        public String name;
        public ArrayList<CartItem> cart;

        public CatalogCategory(int id, String name) {
            this.id = id;
            this.name = name;
            cart = new ArrayList<>();
        }
    }

    public static class CartItem {
        public int id;
        public int category;
        public String name;
        public double price;
        public String description;

        public CartItem(int id, int category, String name, double price, String description) {
            this.id = id;
            this.category = category;
            this.name = name;
            this.price = price;
            this.description = description;
        }
    }
}