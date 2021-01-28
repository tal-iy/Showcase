package app.utils;

import app.api.*;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseAccess {

    private Connection con;

    public DatabaseAccess() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3320/data","root","Blueberry156");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public Account[] getAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();

        // Query DB for the user info
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, username, password, authToken from Accounts;");

            // Build accounts list
            while (rs.next())
                accounts.add(new Account(rs.getInt("id"), rs.getString("username")));

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return accounts.toArray(new Account[accounts.size()]);
    }

    public Account[] getAuthAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();

        // Query DB for the user info
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, username, password, authToken from Accounts where authToken != 'noauth';");

            // Build accounts list
            while (rs.next())
                accounts.add(new Account(rs.getInt("id"), rs.getString("username")));

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return accounts.toArray(new Account[accounts.size()]);
    }

    public FullAccount getAccount(String username) {
        FullAccount account = null;

        // Query DB for the user info
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, username, password, authToken from Accounts where username='"+username+"';");

            // Build Account object if that account exists
            if (rs.next())
                account = new FullAccount(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("authToken"));

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return account;
    }

    public FullAccount getAccount(int userId) {
        FullAccount account = null;

        // Query DB for the user info
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, username, password, authToken from Accounts where id="+userId+";");

            // Build Account object if that account exists
            if (rs.next())
                account = new FullAccount(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("authToken"));

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return account;
    }

    public FullAccount createAccount(String username, String password) {
        FullAccount account = null;

        // Query DB to add an account entry
        try {
            Statement st = con.createStatement();
            st.execute("insert into Accounts (username, password, authToken) values ('"+username+"','"+password+"','noauth');");
            st.close();

            // Retrieve new account
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, username, password, authToken from Accounts where username='"+username+"';");

            // Build Account object
            if (rs.next())
                account = new FullAccount(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("authToken"));

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return account;
    }

    public void updateAccount(int userId, String field, String value) {
        // Query DB to change an account
        try {
            Statement st = con.createStatement();
            st.execute("update Accounts set "+field+"='"+value+"' where id="+userId+";");
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAccount(int id) {
        // Query DB to delete an account
        try {
            Statement st = con.createStatement();
            st.execute("delete from Accounts where id="+id+";");
            st.execute("delete from Carts where user_id="+id+";");
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public CartItem getItem(int itemId) {
        CartItem item = null;

        // Query DB for the item info
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, category, name, price, descr from Items where id="+itemId+";");

            // Build CartItem object if that item exists
            if (rs.next())
                item = new CartItem(rs.getInt("id"), rs.getInt("category"), rs.getString("name"), rs.getDouble("price"), rs.getString("descr"));

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return item;
    }

    public CartItem getCartItem(int itemId) {
        CartItem item = null;

        // Query DB for the cart item info
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select item_id from Carts where id="+itemId+";");

            // Get the item associated with the cart itemId
            if (rs.next()) {

                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery("select id, category, name, price, descr from Items where id="+rs.getInt("item_id")+";");

                if (rs2.next())
                    item = new CartItem(itemId, rs2.getInt("category"), rs2.getString("name"), rs2.getDouble("price"), rs2.getString("descr"));
                st2.close();
            }

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return item;
    }

    public CartItem[] getCart(int userId) {
        ArrayList<CartItem> items = new ArrayList<>();

        // Query DB for the cart info
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, item_id from Carts where user_id="+userId+";");

            // Build item list
            while (rs.next()) {

                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery("select id, category, name, price, descr from Items where id="+rs.getInt("item_id")+";");

                if (rs2.next())
                    items.add(new CartItem(rs.getInt("id"), rs2.getInt("category"), rs2.getString("name"), rs2.getDouble("price"), rs2.getString("descr")));
                st2.close();
            }

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return items.toArray(new CartItem[items.size()]);
    }

    public CartItem addCart(int userId, int itemId) {
        CartItem item = null;

        // Query DB to add item to cart
        try {
            Statement st = con.createStatement();
            st.execute("insert into Carts (item_id, user_id) values ("+itemId+", "+userId+");");
            st.close();

            // Retrieve new item info
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, category, name, price, descr from Items where id="+itemId+";");

            // Build Account object
            if (rs.next())
                item = new CartItem(rs.getInt("id"), rs.getInt("category"), rs.getString("name"), rs.getDouble("price"), rs.getString("descr"));

            st.close();

            // Retrieve new item id
            st = con.createStatement();
            rs = st.executeQuery("select id from Carts where item_id="+itemId+" and user_id="+userId+";");

            if (rs.next() && item != null)
                item.setId(rs.getInt("id"));

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return item;
    }

    public void removeCart(int itemId) {
        // Query DB to delete item from the cart
        try {
            Statement st = con.createStatement();
            st.execute("delete from Carts where id="+itemId+";");
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void clearCart(int userId) {
        // Query DB to clear all items in the cart
        try {
            Statement st = con.createStatement();
            st.execute("delete from Carts where user_id="+userId+";");
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Catalog createCategory(String name) {
        Catalog cat = null;

        // Query DB to add a category entry
        try {
            Statement st = con.createStatement();
            st.execute("insert into Categories (name) values ('"+name+"');");
            st.close();

            // Retrieve new category
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select id from Categories where name='"+name+"';");

            // Build CatalogCategory object
            if (rs.next())
                cat = new Catalog(rs.getInt("id"), name);

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return cat;
    }

    public FullCatalog getCategory(int catId) {
        FullCatalog cat = null;

        // Query DB to add a category entry
        try {
            // Retrieve category
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, name from Categories where id="+catId+";");

            // Build CatalogCategory object
            if (rs.next())
                cat = new FullCatalog(catId, rs.getString("name"));

            st.close();

            // Retrieve all items in the category
            st = con.createStatement();
            rs = st.executeQuery("select id, category, name, price, descr from Items where category="+catId+";");

            ArrayList<CartItem> cart = new ArrayList<>();
            while (rs.next() && cat != null)
                cart.add(new CartItem(rs.getInt("id"), rs.getInt("category"), rs.getString("name"), rs.getDouble("price"), rs.getString("descr")));

            if (cat != null)
                cat.setItems(cart.toArray(new CartItem[cart.size()]));

            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return cat;
    }

    public Catalog[] getCategories() {
        ArrayList<Catalog> cats = new ArrayList<>();

        // Query DB to add a category entry
        try {
            // Retrieve categories
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, name from Categories;");

            // Build CatalogCategory object for every category
            while (rs.next())
                cats.add(new Catalog(rs.getInt("id"), rs.getString("name")));

            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return cats.toArray(new Catalog[cats.size()]);
    }

    public void updateCatagory(int catId, String field, String value) {
        // Query DB to change a category
        try {
            Statement st = con.createStatement();
            st.execute("update Categories set "+field+"='"+value+"' where id="+catId+";");
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteCategory(int catId) {
        // Query DB to add item to cart
        try {
            Statement st = con.createStatement();
            st.execute("delete from Categories where id="+catId+";");
            st.execute("delete from Items where category="+catId+";");
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public CartItem createItem(int catId, String name, double price, String descr) {
        CartItem item = null;

        // Query DB to add item to category
        try {
            Statement st = con.createStatement();
            st.execute("insert into Items (category, name, price, descr) values ("+catId+", '"+name+"', "+price+", '"+descr+"');");
            st.close();

            // Retrieve new item info
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select id from Items where name='"+name+"' and price="+price+" and descr='"+descr+"';");

            // Build item object
            if (rs.next())
                item = new CartItem(rs.getInt("id"), catId, name, price, descr);
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return item;
    }

    public void updateItem(int itemId, String name, double price, String descr) {
        // Query DB to change a category
        try {
            Statement st = con.createStatement();
            st.execute("update Items set name='"+name+"', price="+price+", descr='"+descr+"' where id="+itemId+";");
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteItem(int itemId) {
        // Query DB to change a category
        try {
            Statement st = con.createStatement();
            st.execute("delete from Items where id="+itemId+";");
            st.execute("delete from Carts where item_id="+itemId+";");
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        // Try to close the MySQL connection
        try {
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
