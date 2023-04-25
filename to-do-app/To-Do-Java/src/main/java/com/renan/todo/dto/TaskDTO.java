package com.renan.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transfer Object for Task
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDTO {

    private Integer id;
    private String  taskName;
    private String  taskDescription;
    private String  deadline;
    private Integer taskStatus;

}