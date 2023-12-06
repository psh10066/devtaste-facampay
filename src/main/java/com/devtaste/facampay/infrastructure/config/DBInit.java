package com.devtaste.facampay.infrastructure.config;

import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUser;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUserRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreToUserRepository storeToUserRepository;

    @Override
    public void run(String... args) {

        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String userEmail = "user" + i + "@facam.com";
            String userName = "사용자" + i;
            long money = 20000 - i * 1500;
            userList.add(
                userRepository.findByUserEmail(userEmail).or(() -> Optional.of(userRepository.save(User.of(userEmail, userName, money)))).get()
            );
        }

        List<Store> storeList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String storeEmail = "store" + i + "@facam.com";
            String storeName = "가맹점" + i;
            storeList.add(
                storeRepository.findByStoreEmail(storeEmail).or(() -> Optional.of(storeRepository.save(Store.of(storeEmail, storeName, 0L)))).get()
            );
        }

        // 사용자, 가맹점 등록 정보 섞어서 저장
        for (int i = 0; i < 10; i++) {
            for (int j = 1; j <= 5; j++) {
                storeToUserRepository.save(StoreToUser.of(storeList.get(i), userList.get((i + j) % 10)));
            }
        }
    }
}
