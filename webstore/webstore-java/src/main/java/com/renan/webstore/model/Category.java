package com.renan.webstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@Entity
//@Table(name = "CATEGORY")
public class Category {
    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ID")
    private Integer id;
//    @Column(name = "NAME", nullable = false)
    private String name;
//    @ManyToOne
//    @JoinColumn(name = "ID_UPPER_CATEGORY", nullable = false)
    private String upperCategory;

}
