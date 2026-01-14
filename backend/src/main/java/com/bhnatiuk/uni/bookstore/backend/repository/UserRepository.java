package com.bhnatiuk.uni.bookstore.backend.repository;

import com.bhnatiuk.uni.bookstore.backend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<UserDetails> findAppUserByEmail(String s);
}
