package com.devtaste.facampay.infrastructure.dao.jpa.user;

import com.devtaste.facampay.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail(String userEmail);
}
