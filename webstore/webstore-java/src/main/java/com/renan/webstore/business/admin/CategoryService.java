package com.renan.webstore.business.admin;

import com.renan.webstore.business.BusinessException;
import com.renan.webstore.controller.form.admin.CategoryForm;
import com.renan.webstore.model.Category;

import java.util.List;

public interface CategoryService {
    
    List<Category> getCategories() throws BusinessException;
    
    String createCategory(CategoryForm form) throws BusinessException;
    
    void updateCategory(CategoryForm form, String id) throws BusinessException;
    
    void deleteCategory(String id) throws BusinessException;

}
