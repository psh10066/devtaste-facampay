package com.devtaste.facampay.infrastructure.helper;

import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.domain.service.StoreService;
import com.devtaste.facampay.domain.service.UserService;
import lombok.Getter;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Getter
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentTestHelper {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;

    private StoreService storeService;
    private UserService userService;

    private Store store;
    private User user;

    protected void preparePaymentTest() {
        this.storeRepository.deleteAllInBatch();
        this.userRepository.deleteAllInBatch();
        this.storeService = new StoreService(storeRepository);
        this.userService = new UserService(userRepository);
        this.store = this.storeService.save(Store.builder()
            .storeEmail("store@facam.com")
            .storeName("가맹점1")
            .money(0L)
            .build());
        this.user = this.userService.save(User.builder()
            .userEmail("user@facam.com")
            .userName("사용자1")
            .money(25000L)
            .build());
    }
}
