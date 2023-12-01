package com.devtaste.facampay.domain.service;

import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public Optional<Store> findByStoreEmail(String storeEmail) {
        return storeRepository.findByStoreEmail(storeEmail);
    }

    public Store save(Store store) {
        return storeRepository.save(store);
    }
}
