package com.renan.webstore.dto.mapper

import com.renan.webstore.dto.CategoryDTO
import com.renan.webstore.model.Category
import org.springframework.stereotype.Component
import java.util.function.Function
import kotlin.collections.ArrayList

@Component
class CategoryDTOMapper : Function<Category, CategoryDTO> {

    override fun apply(cat: Category): CategoryDTO {
        return CategoryDTO(
            id = cat.id!!,
            name = cat.name,
            subCategories = null
        )
    }

    fun convertCategoriesToDTO(categories: List<Category>): List<CategoryDTO> {
        val listDTO = ArrayList<CategoryDTO>()
        categories.forEach { cat ->
            if (cat.upperCategory == null) {
                listDTO.add(
                    CategoryDTO(
                        id = cat.id!!,
                        name = cat.name
                    )
                )
            }
        }
        categories.forEach {cat ->
            if (cat.upperCategory != null) {
                listDTO.forEach {dto ->
                    if (cat.upperCategory!!.id == dto.id) {
                        if (dto.subCategories == null) dto.subCategories = ArrayList()
                        (dto.subCategories as ArrayList).add(CategoryDTO(
                            id = cat.id!!,
                            name = cat.name
                        ))
                    }
                }
            }
        }
        return listDTO
    }

}