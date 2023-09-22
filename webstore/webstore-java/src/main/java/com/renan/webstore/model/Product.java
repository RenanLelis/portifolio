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
@Table(name = "PRODUCT")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Column(name = "TECHNICAL_DESCRIPTION")
    private String technicalDescription;
    @Column(name = "PRICE", nullable = false)
    private Double price = 0.0;
    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;
    @Column(name = "INITIAL_PAGE", nullable = false)
    private Boolean initialPage;
    @ManyToOne
    @JoinColumn(name = "ID_IMAGE")
    private Image image;
    
}
