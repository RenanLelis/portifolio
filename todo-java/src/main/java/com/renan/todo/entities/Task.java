package com.renan.todo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * Task Entity class
 */
@Entity
@Table(name = "TASK")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TASK_NAME", nullable = false)
    private String taskName;

    @Column(name = "TASK_DESCRIPTION", nullable = true)
    private String taskDescription;

    @Column(name = "DEADLINE", nullable = true)
    private Date deadline;

    @Column(name = "TASK_STATUS", nullable = false)
    private Integer taskStatus;

    @ManyToOne
    @Column(name = "ID_USER", nullable = true)
    private User user;

    @ManyToOne
    @Column(name = "ID_LIST", nullable = true)
    private List list;

}
