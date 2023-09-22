package com.renan.webstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_RELATED")
public class ProductRelated {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "ID_PRODUCT")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "ID_PRODUCT_RELATED")
    private Product productRelated;
    
}
