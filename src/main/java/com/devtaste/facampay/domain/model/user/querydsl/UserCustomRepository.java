package com.devtaste.facampay.domain.model.user.querydsl;

import com.devtaste.facampay.application.user.dto.UserDTO;
import com.devtaste.facampay.presentation.user.request.UserListRequest;

import java.util.List;

public interface UserCustomRepository {

    List<UserDTO> searchStoreToUserList(Long storeId, UserListRequest request);
}
