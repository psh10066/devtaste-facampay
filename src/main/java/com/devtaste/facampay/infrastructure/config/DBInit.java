package com.devtaste.facampay.infrastructure.config;

import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Override
    public void run(String... args) {
        String userEmail = "user@facam.com";
        userRepository.findByUserEmail(userEmail).or(() -> {
            User user = userRepository.save(User.of(userEmail, "사용자1", 25000L));
            return Optional.of(user);
        });

        String storeEmail = "store@facam.com";
        storeRepository.findByStoreEmail(storeEmail).or(() -> {
            Store store = storeRepository.save(Store.of(storeEmail, "가맹점1", 0L));
            return Optional.of(store);
        });
    }
}
