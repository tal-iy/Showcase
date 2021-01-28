package app.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItem {

    private int id;
    private int category;
    private String name;
    private double price;
    private String description;

    public CartItem() { }

    public CartItem(int id, int category, String name, double price, String description) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public int getCategory() {
        return category;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public double getPrice() {
        return price;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
}
