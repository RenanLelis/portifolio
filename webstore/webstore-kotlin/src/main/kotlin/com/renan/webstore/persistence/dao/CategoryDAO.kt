package com.renan.webstore.persistence.dao

import com.renan.webstore.model.Category

interface CategoryDAO {

    fun findAll(): List<Category>

    fun findById(id: Int): Category?

    fun save(category: Category): Category

    fun delete(id: Int)

}