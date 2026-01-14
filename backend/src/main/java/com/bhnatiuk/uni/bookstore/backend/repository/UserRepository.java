package com.bhnatiuk.uni.bookstore.backend.repository;

import com.bhnatiuk.uni.bookstore.backend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
}
