package com.renan.todo.controller.tasks.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Form for tasks change list
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListMoveForm {
    private Integer idOldList;
    private Integer idNewList;
    private List<Integer> tasks;
}
