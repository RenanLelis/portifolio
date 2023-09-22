package com.renan.webstore.persistence.repository;

import com.renan.webstore.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
    
    Optional<AppUser> findByEmail(String email);
    
}
