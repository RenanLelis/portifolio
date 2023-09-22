package com.renan.webstore.persistence.dao.impl

import com.renan.webstore.model.AppUser
import com.renan.webstore.persistence.dao.UserDAO
import com.renan.webstore.persistence.repository.UserRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserDAOImpl(
    val repository: UserRepository
) : UserDAO{

    override fun findByEmail(email: String): AppUser? {
       val optUser = repository.findByEmail(email)
       return if (optUser.isPresent) optUser.get() else null
    }

    override fun saveUser(user: AppUser): AppUser = repository.save(user)

    override fun updateUser(user: AppUser) { repository.save(user) }

    override fun findById(id: Int): Optional<AppUser> = repository.findById(id)
}