package com.renan.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Transfer Object for TaskList
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListDTO {

    private Integer id;
    private String listName;
    private String listDescription;
    private List<TaskDTO> tasks;

}
