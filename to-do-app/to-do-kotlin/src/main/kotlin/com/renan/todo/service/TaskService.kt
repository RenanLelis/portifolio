package com.renan.todo.service

import com.renan.todo.dto.TaskDTO

/**
 * Services for tasks operation
 */
interface TaskService {

    /**
     * Get the tasks for the list passed
     */
    @Throws(BusinessException::class)
    fun getTasksByList(listID: Int?, userID: Int?): List<TaskDTO?>?

    /**
     * Delete all tasks from the list
     */
    @Throws(BusinessException::class)
    fun deleteTasksFromList(listID: Int?, userID: Int?)

    /**
     * Mark as complete all tasks from the list
     */
    @Throws(BusinessException::class)
    fun completeTasksFromList(listID: Int?, userID: Int?)

    /**
     * Mark as incomplete all tasks from the list
     */
    @Throws(BusinessException::class)
    fun incompleteTasksFromList(listID: Int?, userID: Int?)

    /**
     * Change the listID on all tasks from a list
     */
    @Throws(BusinessException::class)
    fun moveTasksFromList(
        listIDOrigin: Int?,
        listIDDestiny: Int?, userID: Int?
    )

    /**
     * Create a new task for the user
     */
    @Throws(BusinessException::class)
    fun createTask(
        taskName: String?, taskDescription: String?, deadline: String?, listID: Int?, userID: Int?
    ): TaskDTO?

    /**
     * Update a task on the database
     */
    @Throws(BusinessException::class)
    fun updateTask(
        taskName: String?, taskDescription: String?, deadline: String?, taskID: Int?, userID: Int?
    )

    /**
     * change the list for the task
     */
    @Throws(BusinessException::class)
    fun moveTaskToList(taskID: Int?, listIDDestiny: Int?, userID: Int?)

    /**
     * Delete the task on the database
     */
    @Throws(BusinessException::class)
    fun deleteTask(taskID: Int?, userID: Int?)

    /**
     * Mark the task as complete
     */
    @Throws(BusinessException::class)
    fun completeTask(taskID: Int?, userID: Int?)

    /**
     * Mark the task as incomplete
     */
    @Throws(BusinessException::class)
    fun incompleteTask(taskID: Int?, userID: Int?)

}