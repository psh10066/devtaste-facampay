package com.devtaste.facampay.infrastructure.config;

import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.service.StoreService;
import com.devtaste.facampay.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final StoreService storeService;

    @Override
    public void run(String... args) {
        String userEmail = "user@facam.com";
        userService.findByUserEmail(userEmail).or(() -> {
            User user = userService.save(User.builder()
                .userEmail(userEmail)
                .userName("사용자1")
                .money(25000L)
                .build());
            return Optional.of(user);
        });

        String storeEmail = "store@facam.com";
        storeService.findByStoreEmail(storeEmail).or(() -> {
            Store store = storeService.save(Store.builder()
                .storeEmail(storeEmail)
                .storeName("가맹점1")
                .money(0L)
                .build());
            return Optional.of(store);
        });
    }
}
