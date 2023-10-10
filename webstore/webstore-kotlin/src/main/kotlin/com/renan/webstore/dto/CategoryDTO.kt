package com.renan.webstore.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
data class CategoryDTO(
    val id: Int,
    val name: String,
    var subCategories: List<CategoryDTO>? = null
)
