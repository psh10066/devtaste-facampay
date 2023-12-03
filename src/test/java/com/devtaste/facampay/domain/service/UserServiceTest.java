package com.devtaste.facampay.domain.service;

import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
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
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    private User user;

    @BeforeAll
    void before() {
        this.userRepository.deleteAllInBatch();
        this.userService = new UserService(userRepository);
        this.user = this.userService.save(User.builder()
            .userEmail("user@facam.com")
            .userName("사용자1")
            .money(0L)
            .build());
    }

    @DisplayName("ID로 사용자 조회")
    @Test
    void findById() {
        Optional<User> user1 = userService.findById(this.user.getUserId());
        assertTrue(user1.isPresent());
        assertEquals(this.user, user1.get());

        Optional<User> user2 = userService.findById(this.user.getUserId() + 1);
        assertTrue(user2.isEmpty());
    }

    @DisplayName("email로 사용자 조회")
    @Test
    void findByUserEmail() {
        Optional<User> user1 = userService.findByUserEmail("user@facam.com");
        assertTrue(user1.isPresent());
        assertEquals(this.user, user1.get());

        Optional<User> user2 = userService.findByUserEmail("empty@facam.com");
        assertTrue(user2.isEmpty());
    }

    @DisplayName("사용자 저장")
    @Test
    void save() {
        long beforeCount = userRepository.count();

        User user1 = userService.save(User.builder()
            .userEmail("insert@facam.com")
            .userName("신규사용자")
            .money(10000L)
            .build());

        long afterCount = userRepository.count();

        assertNotNull(user1.getUserId());
        assertEquals(beforeCount + 1, afterCount);
    }
}