package com.bhnatiuk.uni.bookstore.backend.repository;

import com.bhnatiuk.uni.bookstore.backend.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
