package com.renan.todo.persistence

import com.renan.todo.model.Task
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Repository for tasks operations
 */
interface TaskRepository : JpaRepository<Task, Int> {

    /**
     * Get the tasks for the list passed
     */
    @Query(value = "Select t From Task t Where t.list.id = :listID And t.user.id = :userID")
    fun getTasksByList(
        @Param("listID") listID: Int,
        @Param("userID") userID: Int
    ): List<Task>?

    /**
     * Get the tasks for the user passed
     */
    @Query(value = "Select t From Task t Where t.user.id = :userID")
    fun getTasksByUser(@Param("userID") userID: Int): List<Task>?

    /**
     * Delete all tasks from the list
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK WHERE ID_LIST = :listID AND ID_USER = :userID")
    fun deleteTasksFromList(
        @Param("listID") listID: Int,
        @Param("userID") userID: Int
    )


    /**
     * Update the task on database
     */
    @Transactional
    @Modifying
    @Query(
        value = "Update Task t Set t.taskName = :taskName, t.taskDescription = :taskDescription, " +
                "deadline = :deadline Where t.id = :id And t.user.id = :userID"
    )
    fun updateTask(
        @Param("taskName") taskName: String,
        @Param("taskDescription") taskDescription: String?,
        @Param("deadline") deadline: String?,
        @Param("id") taskID: Int,
        @Param("userID") userID: Int
    )


    /**
     * delete the task on the database
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK WHERE ID = :id AND ID_USER = :userID")
    fun deleteTask(
        @Param("id") taskID: Int,
        @Param("userID") userID: Int
    )

    /**
     * Change the status for the task
     */
    @Transactional
    @Modifying
    @Query(value = "Update Task t set t.taskStatus = :status Where t.id = :id And t.user.id = :userID")
    fun updateTaskStatus(
        @Param("id") taskID: Int,
        @Param("userID") userID: Int,
        @Param("status") status: Int
    )

    /**
     * change the list for the task on the database
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TASK SET ID_LIST = :listIDDestiny WHERE ID = :id AND ID_USER = :userID")
    fun moveTaskToList(
        @Param("id") taskID: Int,
        @Param("listIDDestiny") listIDDestiny: Int,
        @Param("userID") userID: Int
    )

    /**
     * Change the list id from the tasks of the list
     */
    @Transactional
    @Modifying
    @Query(
        nativeQuery = true,
        value = "UPDATE TASK SET ID_LIST = :listIDDestiny WHERE ID_LIST = :listIDOrigin AND ID_USER = :userID"
    )
    fun moveTasksFromList(
        @Param("listIDOrigin") listIDOrigin: Int,
        @Param("listIDDestiny") listIDDestiny: Int,
        @Param("userID") userID: Int
    )

    /**
     * Update the status for all tasks from a list
     */
    @Transactional
    @Modifying
    @Query(value = "Update Task t set t.taskStatus = :status Where t.list.id = :listID And t.user.id = :userID")
    fun updateStatusTasksFromList(
        @Param("listID") listID: Int,
        @Param("userID") userID: Int,
        @Param("status") status: Int
    )

}