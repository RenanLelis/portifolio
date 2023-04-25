package com.renan.todo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * List Entity class
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TASK_LIST")
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LIST_NAME", nullable = false)
    private String listName;

    @Column(name = "LIST_DESCRIPTION", nullable = true)
    private String listDescription;

    @ManyToOne
    @JoinColumn(name = "ID_USER", nullable = false)
    private User user;
}