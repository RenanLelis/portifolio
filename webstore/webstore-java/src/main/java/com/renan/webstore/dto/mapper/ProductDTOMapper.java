package com.renan.webstore.dto.mapper;


import com.renan.webstore.dto.ProductDTO;
import com.renan.webstore.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ProductDTOMapper implements Function<Product, ProductDTO> {
    
    @Override
    public ProductDTO apply(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getTechnicalDescription(),
                product.getPrice(),
                product.getActive(),
                product.getImage().getId(),
                null
        );
    }
}
