package com.renan.todo.persistence

import com.renan.todo.model.User
import jakarta.transaction.Transactional
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Repository for user operations
 */
interface UserRepository : JpaRepository<User, Int> {

    /**
     * find the user on the database by email
     */
    fun findByEmail(email: String): Optional<User>

    /**
     * Update user info on database
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE User u set u.firstName = :name, u.lastName = :lastName WHERE u.id = :id")
    fun updateUserData(@Param("id") id: Int, @Param("name") name: String, @Param("lastName") lastName: String?)

    /**
     * Update user password
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE User u set u.password = :password WHERE u.id = :id")
    fun updateUserPassword(@Param("id") id: Int, @Param("password") password: String)

}