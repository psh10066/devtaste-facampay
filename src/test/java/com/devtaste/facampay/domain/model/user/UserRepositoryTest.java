package com.devtaste.facampay.domain.model.user;

import com.devtaste.facampay.domain.model.storeToUser.StoreToUserRepository;
import com.devtaste.facampay.infrastructure.helper.RepositoryTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreToUserRepository storeToUserRepository;

    private User user;

    @BeforeAll
    void before() {
        this.storeToUserRepository.deleteAllInBatch();
        this.userRepository.deleteAllInBatch();
        this.user = this.userRepository.save(User.of("user@facam.com", "사용자1", 0L));
    }

    @DisplayName("ID로 사용자 조회")
    @Test
    void findById() {
        Optional<User> user1 = userRepository.findById(this.user.getUserId());
        assertTrue(user1.isPresent());
        assertEquals(this.user, user1.get());

        Optional<User> user2 = userRepository.findById(this.user.getUserId() + 1);
        assertTrue(user2.isEmpty());
    }

    @DisplayName("email로 사용자 조회")
    @Test
    void findByUserEmail() {
        Optional<User> user1 = userRepository.findByUserEmail("user@facam.com");
        assertTrue(user1.isPresent());
        assertEquals(this.user, user1.get());

        Optional<User> user2 = userRepository.findByUserEmail("empty@facam.com");
        assertTrue(user2.isEmpty());
    }

    @DisplayName("사용자 저장")
    @Test
    void save() {
        long beforeCount = userRepository.count();

        User user1 = userRepository.save(User.of("insert@facam.com", "신규사용자", 10000L));

        long afterCount = userRepository.count();

        assertNotNull(user1.getUserId());
        assertEquals(beforeCount + 1, afterCount);
    }
}