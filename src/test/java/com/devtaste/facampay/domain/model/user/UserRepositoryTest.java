package com.devtaste.facampay.domain.model.user;

import com.devtaste.facampay.infrastructure.helper.RepositoryTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeAll
    void before() {
        this.user = this.userRepository.findById(1L).orElseThrow();
    }

    @DisplayName("ID로 사용자 조회")
    @Test
    void findById() {
        Optional<User> user1 = userRepository.findById(this.user.getUserId());
        assertTrue(user1.isPresent());
        assertEquals(this.user, user1.get());

        Optional<User> user2 = userRepository.findById(11L);
        assertTrue(user2.isEmpty());
    }

    @DisplayName("email로 사용자 조회")
    @Test
    void findByUserEmail() {
        Optional<User> user1 = userRepository.findByUserEmail(this.user.getUserEmail());
        assertTrue(user1.isPresent());
        assertEquals(this.user, user1.get());

        Optional<User> user2 = userRepository.findByUserEmail("empty@facam.com");
        assertTrue(user2.isEmpty());
    }

    @DisplayName("사용자 저장")
    @Test
    void save() {
        long beforeCount = userRepository.count();

        User user1 = userRepository.save(User.of("insert@facam.com", "신규사용자", new BigDecimal(10000)));

        long afterCount = userRepository.count();

        assertNotNull(user1.getUserId());
        assertEquals(beforeCount + 1, afterCount);
    }
}