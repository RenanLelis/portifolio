package com.renan.todo.controller;

import com.renan.todo.controller.filter.AuthFilter;
import com.renan.todo.controller.form.MoveTaskToListForm;
import com.renan.todo.controller.form.MoveTasksFromListToListForm;
import com.renan.todo.controller.form.TaskForm;
import com.renan.todo.controller.form.UpdateTaskForm;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.dto.TaskDTO;
import com.renan.todo.service.BusinessException;
import com.renan.todo.service.JwtService;
import com.renan.todo.service.TaskService;
import com.renan.todo.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller for tasks operations
 */
@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;
    private final JwtService jwtService;

    /**
     * Get all tasks on the list
     *
     * @param jwt - user token
     * @param id  - list id
     *
     * @return - list of tasks
     */
    @GetMapping(value = "/api/taskList/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskDTO> getTasksByList(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @PathVariable("id") Integer id) {
        Integer userID = jwtService.getIdUser(jwt);
        return taskService.getTasksByList(id, userID);
    }

    /**
     * Mark all tasks from a list to complete
     *
     * @param jwt - user token
     * @param id  - list id
     */
    @PutMapping(value = "/api/taskList/tasks/complete/{id}")
    public void completeTasksFromList(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @PathVariable("id") Integer id) {
        Integer userID = jwtService.getIdUser(jwt);
        taskService.completeTasksFromList(id, userID);
    }

    /**
     * Mark all tasks from a list to incomplete
     *
     * @param jwt - user token
     * @param id  - list id
     */
    @PutMapping(value = "/api/taskList/tasks/uncomplete/{id}")
    public void uncompleteTasksFromList(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @PathVariable("id") Integer id) {
        Integer userID = jwtService.getIdUser(jwt);
        taskService.incompleteTasksFromList(id, userID);
    }

    /**
     * Change tasks from a list to another
     *
     * @param jwt  - user token
     * @param form - form with data for the operation
     */
    @PutMapping(value = "/api/taskList/tasks/moveFromList",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void moveTasksFromList(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @RequestBody MoveTasksFromListToListForm form) {
        Integer userID = jwtService.getIdUser(jwt);
        taskService.moveTasksFromList(form.getListIdOrigin(), form.getListIdDestiny(), userID);
    }

    /**
     * Create a new task
     *
     * @param jwt  - user token
     * @param form - form with data for the operation
     *
     * @return - task created
     */
    @PostMapping(value = "/api/task",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskDTO createTask(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @RequestBody TaskForm form) {
        Integer userID = jwtService.getIdUser(jwt);
        return taskService.createTask(
                form.getTaskName() != null ? form.getTaskName().trim() : "",
                form.getTaskDescription() != null ? form.getTaskDescription().trim() : "",
                form.getDeadline() != null ? form.getDeadline().trim() : "",
                form.getListId(),
                userID);
    }

    /**
     * Update a task
     *
     * @param jwt  - user token
     * @param id   - task id
     * @param form - form with data for the operation
     */
    @PutMapping(value = "/api/task/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateTask(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @PathVariable("id") Integer id,
            @RequestBody UpdateTaskForm form) {
        Integer userID = jwtService.getIdUser(jwt);
        taskService.updateTask(form.getTaskName(), form.getTaskDescription(), form.getDeadline(), id, userID);
    }

    /**
     * Delete a task
     *
     * @param jwt - user token
     * @param id  - task id
     */
    @DeleteMapping(value = "/api/task/{id}")
    public void deleteTask(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @PathVariable("id") Integer id) {
        Integer userID = jwtService.getIdUser(jwt);
        taskService.deleteTask(id, userID);
    }

    /**
     * Update a task, setting as incomplete
     *
     * @param jwt - user token
     * @param id  - task id
     */
    @PutMapping(value = "/api/task/complete/{id}")
    public void complete(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @PathVariable("id") Integer id) {
        Integer userID = jwtService.getIdUser(jwt);
        taskService.completeTask(id, userID);
    }

    /**
     * Update a task, setting as incomplete
     *
     * @param jwt - user token
     * @param id  - task id
     */
    @PutMapping(value = "/api/task/uncomplete/{id}")
    public void incompleteTask(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @PathVariable("id") Integer id) {
        Integer userID = jwtService.getIdUser(jwt);
        taskService.incompleteTask(id, userID);
    }

    /**
     * Update a task, setting a new listID to it
     *
     * @param jwt  - user token
     * @param id   - task id
     * @param form - form with data for the operation
     */
    @PutMapping(value = "/api/task/list/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void moveTaskToList(
            @RequestHeader(AuthFilter.AUTH) String jwt,
            @PathVariable("id") Integer id,
            @RequestBody MoveTaskToListForm form) {
        Integer userID = jwtService.getIdUser(jwt);
        taskService.moveTaskToList(id, form.getListId(), userID);
    }

    /**
     * Handle de business exceptions and return the response entity with the status code
     *
     * @param e - the exception
     *
     * @return the response entity with the status code and error dto
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessExceptions(BusinessException e) {
        return e.getResponseEntity();
    }

    /**
     * Handle exceptions and return the response entity with the status code
     *
     * @param e - the exception
     *
     * @return the response entity with the status code and error dto
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleExceptions(Exception e) {
        e.printStackTrace();
        if (e instanceof BusinessException) {
            return ((BusinessException) e).getResponseEntity();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(MessageUtil.getErrorMessage()));
    }

}