package com.devtaste.facampay.domain.model.storeToUser;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface StoreToUserRepository extends JpaRepository<StoreToUser, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<StoreToUser> findByStoreStoreIdAndUserUserId(Long userId, Long storeId);

    Optional<StoreToUser> findByStore_StoreIdAndUser_UserId(Long userId, Long storeId);
}
