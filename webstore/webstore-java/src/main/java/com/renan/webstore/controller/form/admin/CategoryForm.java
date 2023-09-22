package com.renan.webstore.controller.form.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryForm {
    
    private String name;
    private String upperCategoryId;
    
}
