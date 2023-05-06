package com.renan.todo.controller

import com.renan.todo.controller.filter.AUTH
import com.renan.todo.controller.form.MoveTaskToListForm
import com.renan.todo.controller.form.MoveTasksFromListToListForm
import com.renan.todo.controller.form.TaskForm
import com.renan.todo.controller.form.UpdateTaskForm
import com.renan.todo.dto.ErrorDTO
import com.renan.todo.service.BusinessException
import com.renan.todo.service.JwtService
import com.renan.todo.service.TaskService
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
 * The controller for tasks operations
 */
@RestController
class TaskController(val taskService: TaskService, val jwtService: JwtService) {

    /**
     * Get all tasks on the list
     */
    @GetMapping(value = ["/api/taskList/tasks/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTasksByList(
        @RequestHeader(AUTH) jwt: String?,
        @PathVariable("id") id: Int?
    ) = taskService.getTasksByList(id, jwtService.getIdUser(jwt))

    /**
     * Mark all tasks from a list to complete
     */
    @PutMapping(value = ["/api/taskList/tasks/complete/{id}"])
    fun completeTasksFromList(
        @RequestHeader(AUTH) jwt: String?,
        @PathVariable("id") id: Int?
    ) = taskService.completeTasksFromList(id, jwtService.getIdUser(jwt))

    /**
     * Mark all tasks from a list to incomplete
     */
    @PutMapping(value = ["/api/taskList/tasks/uncomplete/{id}"])
    fun uncompleteTasksFromList(
        @RequestHeader(AUTH) jwt: String?,
        @PathVariable("id") id: Int?
    ) = taskService.incompleteTasksFromList(id, jwtService.getIdUser(jwt))

    /**
     * Change tasks from a list to another
     */
    @PutMapping(
        value = ["/api/taskList/tasks/moveFromList"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun moveTasksFromList(
        @RequestHeader(AUTH) jwt: String?,
        @RequestBody form: MoveTasksFromListToListForm
    ) = taskService.moveTasksFromList(form.listIdOrigin, form.listIdDestiny, jwtService.getIdUser(jwt))

    /**
     * Create a new task
     */
    @PostMapping(
        value = ["/api/task"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createTask(
        @RequestHeader(AUTH) jwt: String?,
        @RequestBody form: TaskForm
    ) = taskService.createTask(
        form.taskName.trim { it <= ' ' },
        if (form.taskDescription != null) form.taskDescription.trim { it <= ' ' } else "",
        if (form.deadline != null) form.deadline.trim { it <= ' ' } else "",
        form.listId,
        jwtService.getIdUser(jwt))

    /**
     * Update a task
     */
    @PutMapping(
        value = ["/api/task/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateTask(
        @RequestHeader(AUTH) jwt: String?,
        @PathVariable("id") id: Int?,
        @RequestBody form: UpdateTaskForm
    ) = taskService.updateTask(form.taskName, form.taskDescription, form.deadline, id, jwtService.getIdUser(jwt))

    /**
     * Delete a task
     */
    @DeleteMapping(value = ["/api/task/{id}"])
    fun deleteTask(
        @RequestHeader(AUTH) jwt: String?,
        @PathVariable("id") id: Int?
    ) = taskService.deleteTask(id, jwtService.getIdUser(jwt))

    /**
     * Update a task, setting as incomplete
     */
    @PutMapping(value = ["/api/task/complete/{id}"])
    fun complete(
        @RequestHeader(AUTH) jwt: String?,
        @PathVariable("id") id: Int?
    ) = taskService.completeTask(id, jwtService.getIdUser(jwt))

    /**
     * Update a task, setting as incomplete
     */
    @PutMapping(value = ["/api/task/uncomplete/{id}"])
    fun incompleteTask(
        @RequestHeader(AUTH) jwt: String?,
        @PathVariable("id") id: Int?
    ) = taskService.incompleteTask(id, jwtService.getIdUser(jwt))

    /**
     * Update a task, setting a new listID to it
     */
    @PutMapping(
        value = ["/api/task/list/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun moveTaskToList(
        @RequestHeader(AUTH) jwt: String?,
        @PathVariable("id") id: Int?,
        @RequestBody form: MoveTaskToListForm
    ) = taskService.moveTaskToList(id, form.listId, jwtService.getIdUser(jwt))

    /**
     * Handle de business exceptions and return the response entity with the status code
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessExceptions(e: BusinessException) = e.generateResponseEntity()

    /**
     * Handle exceptions and return the response entity with the status code
     */
    @ExceptionHandler(Exception::class)
    fun handleExceptions(e: Exception): ResponseEntity<ErrorDTO> {
        e.printStackTrace()
        return if (e is BusinessException) {
            e.generateResponseEntity()
        } else {
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDTO(getErrorMessage()))
        }
    }

}