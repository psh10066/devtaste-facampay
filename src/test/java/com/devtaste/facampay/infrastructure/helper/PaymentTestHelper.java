package com.devtaste.facampay.infrastructure.helper;

import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class PaymentTestHelper extends RepositoryTest {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;

    private Store store;
    private User user;

    protected void preparePaymentTest() {
        this.storeRepository.deleteAllInBatch();
        this.userRepository.deleteAllInBatch();
        this.store = this.storeRepository.save(Store.of("store@facam.com", "가맹점1", 0L));
        this.user = this.userRepository.save(User.of("user@facam.com", "사용자1", 25000L));
    }
}
