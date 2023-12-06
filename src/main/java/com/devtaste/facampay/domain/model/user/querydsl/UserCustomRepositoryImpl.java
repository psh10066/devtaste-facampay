package com.devtaste.facampay.domain.model.user.querydsl;

import com.devtaste.facampay.application.user.dto.UserDTO;
import com.devtaste.facampay.domain.model.storeToUser.QStoreToUser;
import com.devtaste.facampay.domain.model.user.QUser;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.presentation.user.request.UserListRequest;
import com.querydsl.core.types.Projections;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserCustomRepositoryImpl extends QuerydslRepositorySupport implements UserCustomRepository {

    private final QUser user = QUser.user;
    private final QStoreToUser storeToUser = QStoreToUser.storeToUser;

    public UserCustomRepositoryImpl() {
        super(User.class);
    }

    @Override
    public List<UserDTO> searchStoreToUserList(Long storeId, UserListRequest request) {
        return from(user)
            .select(Projections.constructor(UserDTO.class, user.userId, user.userName, user.userEmail))
            .join(storeToUser).on(storeToUser.user.userId.eq(user.userId))
            .where(
                storeToUser.store.storeId.eq(storeId),
                StringUtils.isNotBlank(request.userName()) ? user.userName.contains(request.userName()) : null,
                StringUtils.isNotBlank(request.userEmail()) ? user.userEmail.contains(request.userEmail()) : null
            )
            .fetch();
    }
}
