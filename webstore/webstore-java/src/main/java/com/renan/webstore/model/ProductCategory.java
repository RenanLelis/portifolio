package com.renan.webstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "PRODUCT_CATEGORY")
public class ProductCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "ID_CATEGORY")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "ID_PRODUCT")
    private Product product;
    
}
