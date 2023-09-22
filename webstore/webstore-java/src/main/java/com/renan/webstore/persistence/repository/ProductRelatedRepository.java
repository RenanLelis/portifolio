package com.renan.webstore.persistence.repository;

import com.renan.webstore.model.Product;
import com.renan.webstore.model.ProductRelated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRelatedRepository extends JpaRepository<ProductRelated, Integer> {
    
    @Query("select pe.productRelated from ProductRelated pr where pr.product.id = :productID")
    List<Product> getRelatedProducts(@Param("productID") Integer productID);
    
}
