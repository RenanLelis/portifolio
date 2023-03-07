package com.renan.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Transfer Object for Task
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Integer id;
    private String taskName;
    private String taskDescription;
    private Date deadline;
    private Integer taskStatus;

}
