package com.renan.webstore.persistence.repository;

import com.renan.webstore.model.Product;
import com.renan.webstore.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    
    @Query("select pc.product from ProductCategory pc where pc.category.id = :id")
    public List<Product> getProductsByCategory(@Param("id") Integer categoryID);
    
}
