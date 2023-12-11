package com.devtaste.facampay.domain.model.store;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Store> findByStoreId(Long storeId);

    Optional<Store> findByStoreEmail(String storeName);
}
