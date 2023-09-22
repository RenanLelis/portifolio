package com.renan.webstore.persistence.repository

import com.renan.webstore.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, Int> {

}