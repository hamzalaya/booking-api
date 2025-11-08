package com.booking.holders;

public final class ApiPaths {

    public static final String V1 = "/api/v1";


    public static final String ACCOUNTS = "/accounts";
    public static final String BOOKINGS = V1 + "/bookings";
    public static final String BLOCKS = V1 + "/blocks";
    public static final String PROPERTIES = V1 + "/properties";

    public static final String AUTH = ACCOUNTS + "/auth";

    public static final String REGISTER = ACCOUNTS + "/register";

    public static final String USERS = "/users";
    public static final String ME = USERS + "/me";

    private ApiPaths() {
    }
}
