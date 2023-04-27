package com.renan.todo.persistence;

import com.renan.todo.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for tasks operations
 */
public interface TaskRepository extends JpaRepository<Task, Integer> {

    /**
     * Get the tasks for the list passed
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @return - list of tasks for the list id and user id
     */
    @Query(value = "Select t From Task t Where t.list.id = :listID And t.user.id = :userID")
    List<Task> getTasksByList(
            @Param("listID") Integer listID,
            @Param("userID") Integer userID);

    /**
     * Get the tasks for the user passed
     *
     * @param userID - user id
     *
     * @return - list of tasks for the user id
     */
    @Query(value = "Select t From Task t Where t.user.id = :userID")
    List<Task> getTasksByUser(@Param("userID") Integer userID);

    /**
     * Delete all tasks from the list
     *
     * @param listID - list id
     * @param userID - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK WHERE ID_LIST = :listID AND ID_USER = :userID")
    void deleteTasksFromList(
            @Param("listID") Integer listID,
            @Param("userID") Integer userID);

//    /**
//     * Create a new task on the database
//     *
//     * @param taskName        - task name
//     * @param taskDescription - description
//     * @param deadline        - task deadline
//     * @param listID          - list of the new task
//     * @param userID          - user id
//     *
//     * @return - the task created
//     */
//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true
//            , value = "INSERT INTO TASK (TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS, ID_USER, ID_LIST) " +
//            "VALUES (:taskName , :taskDescription , :deadline , :userID , :listID)")
//    Task createTask(@Param("taskName") String taskName, @Param("taskDescription") String taskDescription
//            , @Param("deadline") String deadline, @Param("listID") Integer listID, @Param("userID") Integer userID);

    /**
     * Update the task on database
     *
     * @param taskName        - task name
     * @param taskDescription - description
     * @param deadline        - deadline
     * @param taskID          - task id
     * @param userID          - user id
     */
    @Transactional
    @Modifying
    @Query(value = "Update Task t Set t.taskName = :taskName, t.taskDescription = :taskDescription, " +
            "deadline = :deadline Where t.id = :id And t.user.id = :userID")
    void updateTask(
            @Param("taskName") String taskName,
            @Param("taskDescription") String taskDescription,
            @Param("deadline") String deadline,
            @Param("id") Integer taskID,
            @Param("userID") Integer userID);


    /**
     * delete the task on the database
     *
     * @param taskID - task id
     * @param userID - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK WHERE ID = :id AND ID_USER = :userID")
    void deleteTask(
            @Param("id") Integer taskID,
            @Param("userID") Integer userID);

    /**
     * Change the status for the task
     *
     * @param taskID - task id
     * @param userID - user id
     * @param status - new status
     */
    @Transactional
    @Modifying
    @Query(value = "Update Task t set t.taskStatus = :status Where t.id = :id And t.user.id = :userID")
    void updateTaskStatus(
            @Param("id") Integer taskID,
            @Param("userID") Integer userID,
            @Param("status") Integer status);

    /**
     * change the list for the task on the database
     *
     * @param taskID        - task id
     * @param listIDDestiny - new list for the task
     * @param userID        - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TASK SET ID_LIST = :listIDDestiny WHERE ID = :id AND ID_USER = :userID")
    void moveTaskToList(
            @Param("id") Integer taskID,
            @Param("listIDDestiny") Integer listIDDestiny,
            @Param("userID") Integer userID);

    /**
     * Change the list id from the tasks of the list
     *
     * @param listIDOrigin  - list origin
     * @param listIDDestiny - list destiny
     * @param userID        - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TASK SET ID_LIST = :listIDDestiny WHERE ID_LIST = :listIDOrigin AND ID_USER = :userID")
    void moveTasksFromList(
            @Param("listIDOrigin") Integer listIDOrigin,
            @Param("listIDDestiny") Integer listIDDestiny,
            @Param("userID") Integer userID);

    /**
     * Update the status for all tasks from a list
     *
     * @param listID - list id
     * @param userID - user id
     * @param status - new status
     */
    @Transactional
    @Modifying
    @Query(value = "Update Task t set t.taskStatus = :status Where t.list.id = :listID And t.user.id = :userID")
    void updateStatusTasksFromList(
            @Param("listID") Integer listID,
            @Param("userID") Integer userID,
            @Param("status") Integer status);

}