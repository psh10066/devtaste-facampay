package com.devtaste.facampay.presentation.user.request;

public record UserListRequest(String userName, String userEmail) {

    public static UserListRequest of(String userName, String userEmail) {
        return new UserListRequest(userName, userEmail);
    }
}
