package com.shailush.server.dto;

public class ProfileRequest {
    private Long id;

    public ProfileRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
