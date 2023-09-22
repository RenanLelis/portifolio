package com.renan.webstore.persistence.repository;

import com.renan.webstore.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    
    @Query("select i.imageBase64 from Image i where i.id = :id")
    String getImage(@Param("id") Integer id);
    
}
