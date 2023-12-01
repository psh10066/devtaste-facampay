package com.devtaste.facampay.infrastructure.dao.jpa.store;

import com.devtaste.facampay.domain.model.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreJpaRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreEmail(String storeName);
}
