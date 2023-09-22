package com.renan.webstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    
    private Integer id;
    private String name;
    private List<CategoryDTO> subCategories;
    
}
