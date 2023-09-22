package com.renan.webstore.dto.mapper;

import com.renan.webstore.dto.CategoryDTO;
import com.renan.webstore.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class CategoryDTOMapper implements Function<Category, CategoryDTO> {
    
    @Override
    public CategoryDTO apply(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                null
        );
    }
    
    public List<CategoryDTO> convertCategoriesToDTO(List<Category> categories) {
        if (categories == null || categories.isEmpty()) return null;
        List<CategoryDTO> result = new ArrayList<>();
        for (Category cat : categories) {
            if (cat.getUpCategory() == null) {
                result.add(apply(cat));
            }
            categories.remove(cat);
        }
        for (Category cat : categories) {
            for (CategoryDTO dto: result) {
                if (cat.getUpCategory().getId().equals(dto.getId())){
                    if (dto.getSubCategories() == null) dto.setSubCategories(new ArrayList<>());
                    dto.getSubCategories().add(apply(cat));
                }
            }
        }
        return result;
    }
    
}
