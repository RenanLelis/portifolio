package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * form to update tasks from a list, setting them to another
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveTasksFromListToListForm {
    private Integer listIdOrigin;
    private Integer listIdDestiny;
}