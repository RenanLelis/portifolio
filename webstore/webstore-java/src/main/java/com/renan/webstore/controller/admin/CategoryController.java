package com.renan.webstore.controller.admin;

import com.renan.webstore.business.admin.CategoryService;
import com.renan.webstore.controller.form.admin.CategoryForm;
import com.renan.webstore.dto.CategoryDTO;
import com.renan.webstore.dto.mapper.CategoryDTOMapper;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/admin/category")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService service;
    private final CategoryDTOMapper mapper;
    
    @GetMapping
    public List<CategoryDTO> getCategories(){
        return service.getCategories().stream().map(mapper).toList();
    }
    
    @PostMapping
    public String createCategory(@RequestBody CategoryForm form){
        return service.createCategory(form);
    }
    
    @PutMapping("/{id}")
    public void updateCategory(@RequestBody CategoryForm form, @PathParam("id")String id) {
        service.updateCategory(form, id);
    }
    
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathParam("id")String id) {
        service.deleteCategory(id);
    }
    
}
