package com.renan.webstore.model

import jakarta.persistence.*

@Entity
@Table(name = "CATEGORY")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Int? = null,
    @Column(name = "NAME", nullable = false)
    var name: String,
    @ManyToOne
    @JoinColumn(name = "ID_UPPER_CATEGORY")
    var upperCategory: Category?
)
