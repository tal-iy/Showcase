package app.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private int id;
    private String authToken;

    public User() { }

    public User(int id, String authToken) {
        this.id = id;
        this.authToken = authToken;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getAuthToken() {
        return authToken;
    }
}
