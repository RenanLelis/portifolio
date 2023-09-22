package com.renan.webstore.persistence.repository

import com.renan.webstore.model.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository: JpaRepository<AppUser, Int> {

    fun findByEmail(email: String): Optional<AppUser>

}