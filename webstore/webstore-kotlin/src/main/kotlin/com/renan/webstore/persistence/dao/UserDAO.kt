package com.renan.webstore.persistence.dao

import com.renan.webstore.model.AppUser
import java.util.Optional

interface UserDAO {

    fun findByEmail(email: String): AppUser?

    fun saveUser(user: AppUser): AppUser

    fun updateUser(user: AppUser)

    fun findById(id: Int): Optional<AppUser>

}