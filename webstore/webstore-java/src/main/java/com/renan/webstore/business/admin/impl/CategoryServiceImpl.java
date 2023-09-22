package com.renan.webstore.business.admin.impl;

import com.renan.webstore.business.BusinessException;
import com.renan.webstore.business.admin.CategoryService;
import com.renan.webstore.controller.form.admin.CategoryForm;
import com.renan.webstore.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    public List<Category> getCategories() throws BusinessException {
        //TODO
        return null;
    }
    
    public String createCategory(CategoryForm form) throws BusinessException {
        //TODO
        return null;
    }
    
    public void updateCategory(CategoryForm form, String id) throws BusinessException {
        //TODO
    }
    
    public void deleteCategory(String id) throws BusinessException {
        //TODO
    }
    
}
