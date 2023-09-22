package com.renan.webstore.persistence.repository;

import com.renan.webstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    @Query("from Product p where p.active = true and p.initialPage = true")
    public List<Product> getProductsInitialPage();
    
    
}
