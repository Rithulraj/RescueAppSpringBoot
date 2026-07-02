package com.example.resueapp.security.repository;

import com.example.resueapp.security.entity.User;
import com.example.resueapp.security.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhone(String phone);

    boolean existsByRole(UserRole role);

    Optional<User> findByPhone(String phone);
}
