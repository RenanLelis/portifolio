package com.renan.todo.persistence;

import com.renan.todo.model.TaskList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for lists of tasks operations
 */
public interface TaskListRepository extends JpaRepository<TaskList, Integer> {

    /**
     * Get lists by id and user
     *
     * @param userID - user id
     *
     * @return - the list
     */
    @Transactional
    @Modifying
    @Query(value = "Select t from TaskList t Where t.user.id = :userID")
    List<TaskList> getListsByUser(@Param("userID") Integer userID);

    /**
     * Find a list by id and user
     *
     * @param listId - list id
     * @param userID - user id
     *
     * @return - the list
     */
    @Transactional
    @Modifying
    @Query(value = "Select t from TaskList t Where t.id = :id And t.user.id = :userID")
    TaskList getListByIdAndUser(
            @Param("id") Integer listId,
            @Param("userID") Integer userID);

    /**
     * Update the list on the database
     *
     * @param listName        - list name
     * @param listDescription - description
     * @param listID          - list id
     * @param userID          - user id
     */
    @Transactional
    @Modifying
    @Query(value = "Update TaskList tl Set tl.listName = :listName, tl.listDescription = :listDescription" +
            " Where tl.id = :id And tl.user.id = :userID")
    void updateTaskList(
            @Param("listName") String listName,
            @Param("listDescription") String listDescription,
            @Param("id") Integer listID,
            @Param("userID") Integer userID);

    /**
     * Delete the task from the database
     *
     * @param listID - list id
     * @param userID - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK_LIST WHERE ID = :id AND ID_USER = :userID")
    void deleteTaskList(
            @Param("id") Integer listID,
            @Param("userID") Integer userID);

}