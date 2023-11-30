package com.javalab.boot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByusername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}
