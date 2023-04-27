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
     *
     * @return - user with the email
     */
    public Optional<User> findByEmail(String email);

    /**
     * Update user info on database
     *
     * @param id       - user id
     * @param name     - first name
     * @param lastName - last name
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE User u set u.firstName = :name, u.lastName = :lastName WHERE u.id = :id")
    public void updateUserData(@Param("id") Integer id, @Param("name") String name, @Param("lastName") String lastName);

    /**
     * Update user password
     *
     * @param id       - user id
     * @param password - new password
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE User u set u.password = :password WHERE u.id = :id")
    public void updateUserPassword(@Param("id") Integer id, @Param("password") String password);

}