package app.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Catalog {

    private int id;
    private String name;

    public Catalog() { }

    public Catalog(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}
