package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for update of task
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskForm {

    private String taskName;
    private String taskDescription;
    private String deadline;

}