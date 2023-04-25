package com.renan.todo.persistence;

import com.renan.todo.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository for user operations
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * find the user on the database by email
     *
     * @param email - user email
     * @return
     */
    public Optional<User> findByEmail(String email);

    /**
     * Set a new password code for the user
     *
     * @param email           - user email
     * @param newPasswordCode - the new password code
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE User u set u.newPasswordCode = :newPasswordCode WHERE u.email = :email")
    public void setNewPasswordCode(@Param("email") String email, @Param("newPasswordCode") String newPasswordCode);



}