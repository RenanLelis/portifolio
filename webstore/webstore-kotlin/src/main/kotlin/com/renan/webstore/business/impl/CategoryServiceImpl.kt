package com.renan.webstore.business.impl

import com.renan.webstore.business.AppErrorType
import com.renan.webstore.business.BusinessException
import com.renan.webstore.business.CategoryService
import com.renan.webstore.controller.admin.form.CategoryForm
import com.renan.webstore.dto.CategoryDTO
import com.renan.webstore.dto.mapper.CategoryDTOMapper
import com.renan.webstore.model.Category
import com.renan.webstore.persistence.dao.CategoryDAO
import com.renan.webstore.util.getErrorMessageInputValues
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    val categoryDAO: CategoryDAO,
    val categoryDTOMapper: CategoryDTOMapper
) : CategoryService {

    override fun getCategories(): List<CategoryDTO> {
        val categories = categoryDAO.findAll()
        return categoryDTOMapper.convertCategoriesToDTO(categories)
    }

    override fun createCategory(form: CategoryForm): Int {
        if (form.name.isBlank()) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        var cat: Category? = null
        if (form.idUpperCategory != null) {
            cat = categoryDAO.findById(form.idUpperCategory)
                ?: throw BusinessException(
                    message = getErrorMessageInputValues(),
                    businessMessage = true,
                    errorType = AppErrorType.INVALID_INPUT
                )
        }
        return categoryDAO.save(Category(
            name = form.name,
            upperCategory = cat
        ))
    }

    override fun updateCategory(form: CategoryForm, id: Int) {
        if (form.name.isBlank() || id <= 0 || categoryDAO.findById(id) == null) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        var upperCat: Category? = null
        if (form.idUpperCategory != null) {
            upperCat = categoryDAO.findById(form.idUpperCategory)
                ?: throw BusinessException(
                    message = getErrorMessageInputValues(),
                    businessMessage = true,
                    errorType = AppErrorType.INVALID_INPUT
                )
        }
        categoryDAO.save(Category(
            id = id,
            name = form.name,
            upperCategory = upperCat
        ))
    }

    override fun deleteCategory(id: Int) {
        if (id <= 0 || categoryDAO.findById(id) == null) throw BusinessException(
            message = getErrorMessageInputValues(),
            businessMessage = true,
            errorType = AppErrorType.INVALID_INPUT
        )
        categoryDAO.delete(id)
    }
}