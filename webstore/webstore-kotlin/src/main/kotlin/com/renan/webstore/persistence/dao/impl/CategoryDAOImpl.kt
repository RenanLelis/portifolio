package com.renan.webstore.persistence.dao.impl

import com.renan.webstore.model.Category
import com.renan.webstore.persistence.dao.CategoryDAO
import com.renan.webstore.persistence.repository.CategoryRepository
import org.springframework.stereotype.Repository

@Repository
class CategoryDAOImpl(
    val repository: CategoryRepository
) : CategoryDAO {

    override fun findAll(): List<Category> = repository.findAll()

    override fun findById(id: Int): Category? {
        val opt = repository.findById(id)
        return if (opt.isPresent) opt.get() else null
    }

    override fun save(category: Category): Int = repository.save(category).id!!

    override fun delete(id: Int) {
        repository.deleteById(id)
    }

}