package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for task operations
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskForm {

    private String  taskName;
    private String  taskDescription;
    private String  deadline;
    private Integer idList;

}