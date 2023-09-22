package com.renan.webstore.dto.mapper;

import com.renan.webstore.dto.ProductAdDTO;
import com.renan.webstore.model.ProductAd;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductAdDTOMapper implements Function<ProductAd, ProductAdDTO> {
    
    @Override
    public ProductAdDTO apply(ProductAd productAd) {
        return new ProductAdDTO(
                productAd.getId(),
                productAd.getName(),
                productAd.getImage().getId(),
                productAd.getActive()
        );
    }
}
