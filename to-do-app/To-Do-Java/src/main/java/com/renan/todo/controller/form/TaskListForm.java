package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for creation of task lists
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListForm {
    private String listName;
    private String listDescription;

}