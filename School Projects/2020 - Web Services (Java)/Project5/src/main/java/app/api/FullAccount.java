package app.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class FullAccount {

    private int id;
    private String username;
    private String password;
    private String authToken;

    public FullAccount() { }

    public FullAccount(int id, String username, String password, String authToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authToken = authToken;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @JsonProperty
    public String getAuthToken() {
        return authToken;
    }
}
