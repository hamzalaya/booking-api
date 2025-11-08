package com.booking.security.service;


public class JwtAccessToken {

    private String accessToken;

    public JwtAccessToken() {
    }

    public JwtAccessToken(String token) {
        this.accessToken = token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public JwtAccessToken setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

}
