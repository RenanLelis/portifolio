package com.renan.todo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Task Entity class
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TASK")
public class Task {

    public static final Integer STATUS_INCOMPLETE = 0;
    public static final Integer STATUS_COMPLETE   = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TASK_NAME", nullable = false)
    private String taskName;

    @Column(name = "TASK_DESCRIPTION")
    private String taskDescription;

    @Column(name = "DEADLINE")
    private String deadline;

    @Column(name = "TASK_STATUS", nullable = false)
    private Integer taskStatus;

    @ManyToOne
    @JoinColumn(name = "ID_USER", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ID_LIST")
    private TaskList list;
}