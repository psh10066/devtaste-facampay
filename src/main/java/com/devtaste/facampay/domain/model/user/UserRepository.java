package com.devtaste.facampay.domain.model.user;

import com.devtaste.facampay.domain.model.user.querydsl.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    Optional<User> findByUserEmail(String userEmail);
}
