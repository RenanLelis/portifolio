package com.renan.todo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * List Entity class
 */
@Entity
@Table(name = "LIST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LIST_NAME", nullable = false)
    private String listName;

    @Column(name = "LIST_DESCRIPTION", nullable = true)
    private String listDescription;

    @ManyToOne
    @Column(name = "ID_USER", nullable = false)
    private User user;

}
