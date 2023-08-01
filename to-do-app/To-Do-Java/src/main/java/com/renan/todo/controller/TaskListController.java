package com.renan.todo.controller;

import com.renan.todo.controller.filter.AuthFilter;
import com.renan.todo.controller.form.TaskListForm;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.dto.TaskListDTO;
import com.renan.todo.service.BusinessException;
import com.renan.todo.service.JwtService;
import com.renan.todo.service.TaskListService;
import com.renan.todo.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller for lists of tasks operations
 */
@RequiredArgsConstructor
@RestController
public class TaskListController {

    private final TaskListService taskListService;
    private final JwtService jwtService;

    /**
     * Delete a task list for the user with its tasks
     *
     * @param jwt - token of user
     * @param id  - list id
     */
    @DeleteMapping(value = "/api/taskList/{id}")
    public void deleteTaskList(@RequestHeader(AuthFilter.AUTH) String jwt, @PathVariable("id") Integer id) {
        Integer userID = jwtService.getIdUser(jwt);
        taskListService.deleteTaskList(id, userID);
    }

    /**
     * Update a task list for the user
     *
     * @param jwt  - token of user
     * @param form - input data for list creation
     * @param id   - list id
     */
    @PutMapping(value = "/api/taskList/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateTaskList(@RequestHeader(AuthFilter.AUTH) String jwt, @RequestBody TaskListForm form, @PathVariable("id") Integer id) {
        Integer userID = jwtService.getIdUser(jwt);
        taskListService.updateTaskList(form.getListName(), form.getListDescription(), id, userID);
    }

    /**
     * Create a new task list for the user
     *
     * @param jwt  - token of user
     * @param form - input data for list creation
     *
     * @return - the new list
     */
    @PostMapping(value = "/api/taskList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskListDTO createTaskList(@RequestHeader(AuthFilter.AUTH) String jwt, @RequestBody TaskListForm form) {
        Integer userID = jwtService.getIdUser(jwt);
        return taskListService.createList(form.getListName(), form.getListDescription(), userID);
    }

    /**
     * get lists and tasks for the user
     *
     * @param jwt - token of user
     *
     * @return - the lists
     */
    @GetMapping(value = "/api/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskListDTO> getLists(@RequestHeader(AuthFilter.AUTH) String jwt) {
        Integer userID = jwtService.getIdUser(jwt);
        return taskListService.getLists(userID);
    }

    /**
     * get lists for the user
     *
     * @param jwt - token of user
     *
     * @return - the lists
     */
    @GetMapping(value = "/api/taskList", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskListDTO> getTasksAndLists(@RequestHeader(AuthFilter.AUTH) String jwt) {
        Integer userID = jwtService.getIdUser(jwt);
        return taskListService.getTasksAndLists(userID);
    }

}