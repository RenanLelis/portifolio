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
 * Task Entity class
 */
@Entity
@Table(name = "TASK")
class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Int? = null

    @Column(name = "TASK_NAME", nullable = false)
    var taskName: String? = null

    @Column(name = "TASK_DESCRIPTION")
    var taskDescription: String? = null

    @Column(name = "DEADLINE")
    var deadline: String? = null

    @Column(name = "TASK_STATUS", nullable = false)
    var taskStatus: Int? = null

    @ManyToOne
    @JoinColumn(name = "ID_USER", nullable = false)
    var user: User? = null

    @ManyToOne
    @JoinColumn(name = "ID_LIST")
    var list: TaskList? = null
}