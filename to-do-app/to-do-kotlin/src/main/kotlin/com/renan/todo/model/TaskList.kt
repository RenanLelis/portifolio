package com.renan.todo.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

/**
 * List Entity class
 */
@Entity
@Table(name = "TASK_LIST")
class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Int? = null

    @Column(name = "LIST_NAME", nullable = false)
    var listName: String? = null

    @Column(name = "LIST_DESCRIPTION", nullable = true)
    var listDescription: String? = null

    @ManyToOne
    @JoinColumn(name = "ID_USER", nullable = false)
    var user: User? = null

}