package com.renan.webstore.persistence.repository;

import com.renan.webstore.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    
    @Query("select pi.image.id from ProductImage pi where pi.product.id = :productID")
    List<Integer> getImagesFromProduct(@Param("productID") Integer productID);
    
}
