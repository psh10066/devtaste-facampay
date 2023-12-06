package com.devtaste.facampay.application.user.dto;

import com.devtaste.facampay.domain.model.user.User;

public record UserDTO(Long userId, String userName, String userEmail) {

    public static UserDTO from(User user) {
        return new UserDTO(user.getUserId(), user.getUserName(), user.getUserEmail());
    }

    public static UserDTO of(Long userId, String userName, String userEmail) {
        return new UserDTO(userId, userName, userEmail);
    }
}
