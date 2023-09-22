package com.renan.webstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAdDTO {
    
    private Integer id;
    private String name;
    private Integer image;
    private Boolean active;
    
}
