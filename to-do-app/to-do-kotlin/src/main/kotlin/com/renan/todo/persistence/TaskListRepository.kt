package com.renan.todo.persistence

import com.renan.todo.model.TaskList
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Repository for lists of tasks operations
 */
interface TaskListRepository : JpaRepository<TaskList, Int> {

    /**
     * Get lists by id and user
     */
    @Transactional
    @Modifying
    @Query(value = "Select t from TaskList t Where t.user.id = :userID")
    fun getListsByUser(@Param("userID") userID: Int): List<TaskList>?

    /**
     * Find a list by id and user
     */
    @Query(value = "Select t from TaskList t Where t.id = :id And t.user.id = :userID")
    fun getListByIdAndUser(
        @Param("id") listId: Int,
        @Param("userID") userID: Int
    ): TaskList?

    /**
     * Update the list on the database
     */
    @Transactional
    @Modifying
    @Query(
        value = "Update TaskList tl Set tl.listName = :listName, tl.listDescription = :listDescription" +
                " Where tl.id = :id And tl.user.id = :userID"
    )
    fun updateTaskList(
        @Param("listName") listName: String,
        @Param("listDescription") listDescription: String?,
        @Param("id") listID: Int,
        @Param("userID") userID: Int
    )

    /**
     * Delete the task from the database
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK_LIST WHERE ID = :id AND ID_USER = :userID")
    fun deleteTaskList(
        @Param("id") listID: Int,
        @Param("userID") userID: Int
    )

}