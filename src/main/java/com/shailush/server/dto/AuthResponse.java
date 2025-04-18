package com.shailush.server.dto;

public class AuthResponse {
    private String token;
    private Long id;
    private String email;

    public AuthResponse(String token, Long id, String email) {
        this.token = token;
        this.id = id;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
