package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form to update a list set to a task
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveTaskToListForm {
    private Integer listId;
}