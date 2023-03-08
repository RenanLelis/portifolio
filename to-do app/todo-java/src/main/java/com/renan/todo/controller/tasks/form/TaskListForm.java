package com.renan.todo.controller.tasks.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for taskList creation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListForm {

    private String listName;
    private String listDescription;

}
