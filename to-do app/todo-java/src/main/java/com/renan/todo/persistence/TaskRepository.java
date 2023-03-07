package com.renan.todo.persistence;

import com.renan.todo.entities.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

/**
 * Repository for tasks operations
 */
public interface TaskRepository extends JpaRepository<Task, Integer> {
    
    /**
     * Get the tasks from user without a list
     *
     * @param idUser - user identifier
     * @return - tasks from the user without list
     */
    @Query(value = "SELECT t.id, t.taskName, t.taskDescription, t.deadline, t.taskStatus, t.list FROM Task t AND t.user.id = :idUser")
    public List<Task> findTaskByUser(@Param("idUser") Integer idUser);

    /**
     * Get the tasks from user without a list
     *
     * @param idUser - user identifier
     * @return - tasks from the user without list
     */
    @Query(value = "SELECT t.id, t.taskName, t.taskDescription, t.deadline, t.taskStatus FROM Task t where t.list = null AND t.user.id = :idUser")
    public List<Task> findTaskWithouListByUser(@Param("idUser") Integer idUser);

    /**
     * Get the tasks from a list
     *
     * @param idList - list identifier
     * @return - tasks from the list
     */
    @Query(value = "SELECT t.id, t.taskName, t.taskDescription, t.deadline, t.taskStatus FROM Task t where t.list.id = :idList")
    public List<Task> findByList(@Param("idList") Integer idList);

    /**
     * Delete all the tasks whitout a list
     *
     * @param idUser - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK WHERE ID_LIST = NULL AND ID_USER = :idUser")
    public void deleteTasksWithoutList(@Param("idUser") Integer idUser);

    /**
     * Delete all the tasks from the taskList
     *
     * @param idList - list id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK WHERE ID_LIST = :idList")
    public void deleteTasksFromList(@Param("idList") Integer idList);

    /**
     * Set the tasks to have no list, so it is associated direct to the user
     *
     * @param idTaskListOrigin - taksList where the tasks will be moved from
     * @param idUser           - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TASK SET ID_LIST = null, ID_USER = :idUser WHERE ID = :idTaskListOrigin")
    public void moveTaskstoDefaultList(@Param("idTaskListOrigin") Integer idTaskListOrigin, @Param("idUser") Integer idUser);

    /**
     * Change the tasks from one list to another
     *
     * @param idTaskListOrigin  - taksList where the tasks will be moved from
     * @param idTaskListDestiny - taksList where the tasks will be moved to
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TASK SET ID_LIST = :idTaskListDestiny WHERE ID_LIST = :idTaskListOrigin")
    public void moveTaskstoAnotheList(@Param("idTaskListOrigin") Integer idTaskListOrigin, @Param("idTaskListDestiny") Integer idTaskListDestiny);

    /**
     * Update the task
     *
     * @param id              - task id
     * @param taskName        - task name
     * @param taskDescription - task description
     * @param deadline        - task deadline
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE Task t set t.taskName = :taskName, t.taskDescription = :taskDescription, t.deadline = :deadline where t.id = :id")
    public void updateTask(@Param("id") Integer id, @Param("taskName") String taskName, @Param("taskDescription") String taskDescription, @Param("deadline") Date deadline);

    /**
     * Update the task status
     *
     * @param id     - task id
     * @param status - new status
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE Task t set t.status = :status where t.id = :id")
    public void updateStatusTask(@Param("id") Integer id, @Param("status") Integer status);

}
