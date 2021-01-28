package pkg;
import javax.crypto.spec.DESedeKeySpec;
import java.util.ArrayList;

public class DatabaseTypes {

    public static class Account {
        public String username;
        public String password;
        public int id;
        public ArrayList<CartItem> cart;
        public int cart_nextId;

        public Account(String username, String password, int id) {
            this.username = username;
            this.password = password;
            this.id = id;
            cart = new ArrayList();
            cart_nextId = 0;
        }
    }

    public static class User {
        public String authToken;
        public int id;

        public User(String authToken, int id) {
            this.authToken = authToken;
            this.id = id;
        }
    }

    public static class CatalogCategory {
        public int id;
        public String name;
        public ArrayList<CartItem> cart;
        public int cart_nextId;

        public CatalogCategory(String name, int id) {
            this.name = name;
            this.id = id;
            cart = new ArrayList<>();
            cart_nextId = 0;
        }
    }

    public static class CartItem {
        public int id;
        public String name;
        public double price;
        public String description;
        public int category;

        public CartItem(String name, int id, double price, String description, int category) {
            this.name = name;
            this.id = id;
            this.price = price;
            this.description = description;
            this.category = category;
        }
    }
}