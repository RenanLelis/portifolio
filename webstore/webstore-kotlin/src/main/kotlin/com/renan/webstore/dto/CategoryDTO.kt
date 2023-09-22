package com.renan.webstore.dto

data class CategoryDTO(
    val id: Int,
    val name: String,
    var subCategories: List<CategoryDTO>? = null
)
