package com.devtaste.facampay.domain.model.store;

import com.devtaste.facampay.infrastructure.helper.RepositoryTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StoreRepositoryTest extends RepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    private Store store;

    @BeforeAll
    void before() {
        this.store = storeRepository.findById(1L).orElseThrow();
    }

    @DisplayName("ID로 가맹점 조회")
    @Test
    void findById() {
        Optional<Store> store1 = storeRepository.findById(this.store.getStoreId());
        assertTrue(store1.isPresent());
        assertEquals(this.store, store1.get());

        Optional<Store> store2 = storeRepository.findById(11L);
        assertTrue(store2.isEmpty());
    }

    @DisplayName("email로 가맹점 조회")
    @Test
    void findByStoreEmail() {
        Optional<Store> store1 = storeRepository.findByStoreEmail(this.store.getStoreEmail());
        assertTrue(store1.isPresent());
        assertEquals(this.store, store1.get());

        Optional<Store> store2 = storeRepository.findByStoreEmail("empty@facam.com");
        assertTrue(store2.isEmpty());
    }

    @DisplayName("가맹점 저장")
    @Test
    void save() {
        long beforeCount = storeRepository.count();

        Store store1 = storeRepository.save(Store.of("insert@facam.com", "신규가맹점", 10000L));

        long afterCount = storeRepository.count();

        assertNotNull(store1.getStoreId());
        assertEquals(beforeCount + 1, afterCount);
    }
}