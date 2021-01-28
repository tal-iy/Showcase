package app.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FullCatalog {

    private int id;
    private String name;
    private CartItem[] items;

    public FullCatalog() { }

    public FullCatalog(int id, String name, CartItem[] items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }

    public FullCatalog(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setItems(CartItem[] items) {
        this.items = items;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public CartItem[] getItems() {
        return items;
    }
}
