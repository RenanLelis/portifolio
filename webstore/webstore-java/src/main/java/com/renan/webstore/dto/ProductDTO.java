package com.renan.webstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    
    private Integer id;
    private String name;
    private String description;
    private String tecnicalDescription;
    private Double price;
    private Boolean active;
    private Integer image;
    private List<Integer> images;
    
}
