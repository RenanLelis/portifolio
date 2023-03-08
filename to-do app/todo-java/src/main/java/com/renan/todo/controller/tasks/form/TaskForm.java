package com.renan.todo.controller.tasks.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Form for task creation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskForm {

    private String taskName;
    private String taskDescription;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date deadline;
    private Integer idList;

}
