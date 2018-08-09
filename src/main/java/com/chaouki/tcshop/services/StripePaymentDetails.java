package com.chaouki.tcshop.services;

public class StripePaymentDetails {

    private String token;
    private String tokenType;
    private String email;

    public StripePaymentDetails(String token, String tokenType, String email) {
        this.token = token;
        this.tokenType = tokenType;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getEmail() {
        return email;
    }
}
