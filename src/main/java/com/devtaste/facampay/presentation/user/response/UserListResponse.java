package com.devtaste.facampay.presentation.user.response;

import com.devtaste.facampay.application.user.dto.UserDTO;

import java.util.List;

public record UserListResponse(List<UserDTO> userList) {

    public static UserListResponse of(List<UserDTO> userList) {
        return new UserListResponse(userList);
    }
}
