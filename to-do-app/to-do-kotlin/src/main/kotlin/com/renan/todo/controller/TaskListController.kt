package com.renan.todo.controller

import com.renan.todo.controller.filter.AUTH
import com.renan.todo.controller.form.TaskListForm
import com.renan.todo.dto.ErrorDTO
import com.renan.todo.service.BusinessException
import com.renan.todo.service.JwtService
import com.renan.todo.service.TaskListService
import com.renan.todo.util.getErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for task list requests
 */
@RestController
class TaskListController(val taskListService: TaskListService, val jwtService: JwtService) {

    /**
     * Delete a task list for the user with its tasks
     */
    @DeleteMapping(value = ["/api/taskList/{id}"])
    fun deleteTaskList(@RequestHeader(AUTH) jwt: String?, @PathVariable("id") id: Int?) =
        taskListService.deleteTaskList(id, jwtService.getIdUser(jwt))

    /**
     * Update a task list for the user
     */
    @PutMapping(value = ["/api/taskList/{id}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateTaskList(
        @RequestHeader(AUTH) jwt: String?,
        @RequestBody form: TaskListForm,
        @PathVariable("id") id: Int?
    ) = taskListService.updateTaskList(form.listName, form.listDescription, id, jwtService.getIdUser(jwt))

    /**
     * Create a new task list for the user
     */
    @PostMapping(
        value = ["/api/taskList"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createTaskList(@RequestHeader(AUTH) jwt: String?, @RequestBody form: TaskListForm) =
        taskListService.createList(form.listName, form.listDescription, jwtService.getIdUser(jwt))


    /**
     * get lists and tasks for the user
     */
    @GetMapping(value = ["/api/list"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLists(@RequestHeader(AUTH) jwt: String?) = taskListService.getLists(jwtService.getIdUser(jwt))

    /**
     * get lists for the user
     */
    @GetMapping(value = ["/api/taskList"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTasksAndLists(@RequestHeader(AUTH) jwt: String?) =
        taskListService.getTasksAndLists(jwtService.getIdUser(jwt))

}