package com.devtaste.facampay.domain.service;

import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StoreServiceTest {

    @Autowired
    private StoreRepository storeRepository;

    private StoreService storeService;

    private Store store;

    @BeforeAll
    void before() {
        this.storeRepository.deleteAllInBatch();
        this.storeService = new StoreService(storeRepository);
        this.store = this.storeService.save(Store.builder()
            .storeEmail("store@facam.com")
            .storeName("가맹점1")
            .money(0L)
            .build());
    }

    @DisplayName("ID로 가맹점 조회")
    @Test
    void findById() {
        Optional<Store> store1 = storeService.findById(this.store.getStoreId());
        assertTrue(store1.isPresent());
        assertEquals(this.store, store1.get());

        Optional<Store> store2 = storeService.findById(this.store.getStoreId() + 1);
        assertTrue(store2.isEmpty());
    }

    @DisplayName("email로 가맹점 조회")
    @Test
    void findByStoreEmail() {
        Optional<Store> store1 = storeService.findByStoreEmail("store@facam.com");
        assertTrue(store1.isPresent());
        assertEquals(this.store, store1.get());

        Optional<Store> store2 = storeService.findByStoreEmail("empty@facam.com");
        assertTrue(store2.isEmpty());
    }

    @DisplayName("가맹점 저장")
    @Test
    void save() {
        long beforeCount = storeRepository.count();

        Store store1 = storeService.save(Store.builder()
            .storeEmail("insert@facam.com")
            .storeName("신규가맹점")
            .money(10000L)
            .build());

        long afterCount = storeRepository.count();

        assertNotNull(store1.getStoreId());
        assertEquals(beforeCount + 1, afterCount);
    }
}