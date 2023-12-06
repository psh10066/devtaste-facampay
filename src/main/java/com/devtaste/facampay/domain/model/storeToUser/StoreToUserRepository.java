package com.devtaste.facampay.domain.model.storeToUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreToUserRepository extends JpaRepository<StoreToUser, Long> {

    Optional<StoreToUser> findByStore_StoreIdAndUser_UserId(Long userId, Long storeId);
}
