package com.renan.webstore.persistence.repository;

import com.renan.webstore.model.ProductAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductAdRepository extends JpaRepository<ProductAd, Integer>  {
    
    @Query("from ProductAd pa where pa.active = true")
    public List<ProductAd> getActiveAds();
    
}
