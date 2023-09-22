package com.renan.webstore.controller.admin

import com.renan.webstore.business.CategoryService
import com.renan.webstore.controller.admin.form.CategoryForm
import com.renan.webstore.dto.CategoryDTO
import jakarta.websocket.server.PathParam
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/category")
class CategoryController(
    val categoryService: CategoryService
) {

    @GetMapping
    fun getCategories(): List<CategoryDTO> = categoryService.getCategories()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@RequestBody form: CategoryForm): String = categoryService.createCategory(form)

    @PutMapping("/{id}")
    fun updateCategory(
        @RequestBody form: CategoryForm,
        @PathParam("id") id: String
    ) = categoryService.updateCategory(form, id)

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathParam("id") id: String) = categoryService.deleteCategory(id)

}