package com.devtaste.facampay.domain.model.user;

import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.user.querydsl.UserCustomRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findByUserId(Long userId);

    Optional<User> findByUserEmail(String userEmail);
}
