package com.renan.todo.service

import com.renan.todo.dto.TaskListDTO

/**
 * Services for taskLists operation
 */
interface TaskListService {

    /**
     * Get the tasks and lists for the user
     */
    @Throws(BusinessException::class)
    fun getTasksAndLists(userID: Int?): List<TaskListDTO>

    /**
     * Get the lists for the user
     */
    @Throws(BusinessException::class)
    fun getLists(userID: Int?): List<TaskListDTO>

    /**
     * Create a new list for the user
     */
    @Throws(BusinessException::class)
    fun createList(listName: String?, listDescription: String?, userID: Int?): TaskListDTO

    /**
     * Create a default list for a new user on the system
     */
    @Throws(BusinessException::class)
    fun createDefaultListForNewUser(userID: Int?)

    /**
     * Update name and description for the list
     */
    @Throws(BusinessException::class)
    fun updateTaskList(listName: String?, listDescription: String?, listID: Int?, userID: Int?)

    /**
     * Delete the list and the tasks for the user
     */
    @Throws(BusinessException::class)
    fun deleteTaskList(listID: Int?, userID: Int?)

}