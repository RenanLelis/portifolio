package com.renan.webstore.business

import com.renan.webstore.controller.admin.form.CategoryForm
import com.renan.webstore.dto.CategoryDTO

interface CategoryService {

    @Throws(BusinessException::class)
    fun getCategories(): List<CategoryDTO>

    @Throws(BusinessException::class)
    fun createCategory(form: CategoryForm): Int

    @Throws(BusinessException::class)
    fun updateCategory(form: CategoryForm, id: Int)

    @Throws(BusinessException::class)
    fun deleteCategory(id: Int)

}