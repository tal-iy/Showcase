package app.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {

    private int id;
    private String username;

    public Account() { }

    public Account(int id, String username) {
        this.id = id;
        this.username = username;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }
}
