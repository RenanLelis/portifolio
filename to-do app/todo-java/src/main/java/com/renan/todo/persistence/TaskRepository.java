package com.renan.todo.persistence;

import com.renan.todo.entities.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
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
    @Query(value = "SELECT t.id, t.taskName, t.taskDescription, t.deadline, t.taskStatus FROM Task t where t.list.id = :idList AND t.list.user.id = :idUser")
    public List<Task> findByListAndUser(@Param("idList") Integer idList, @Param("idUser") Integer idUser);

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
     * @param idUser - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK WHERE ID_LIST = :idList AND ID_USER = :idUser")
    public void deleteTasksFromList(@Param("idList") Integer idList, @Param("idUser") Integer idUser);

    /**
     * Set the tasks to have no list, so it is associated direct to the user
     *
     * @param idTaskListOrigin - taksList where the tasks will be moved from
     * @param idUser           - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TASK SET ID_LIST = null WHERE ID_LIST = :idTaskListOrigin AND ID_USER = :idUser)")
    public void moveTasksToDefaultList(@Param("idTaskListOrigin") Integer idTaskListOrigin, @Param("idUser") Integer idUser);

    /**
     * Change the tasks from one list to another
     *
     * @param idTaskListOrigin  - tasksList where the tasks will be moved from
     * @param idTaskListDestiny - tasksList where the tasks will be moved to
     * @param idUser            - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TASK SET ID_LIST = :idTaskListDestiny WHERE ID_LIST = :idTaskListOrigin AND ID_USER = :idUser")
    public void moveTasksToAnotherList(@Param("idTaskListOrigin") Integer idTaskListOrigin, @Param("idTaskListDestiny") Integer idTaskListDestiny, @Param("idUser") Integer idUser);

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
    @Query(value = "UPDATE Task t set t.taskName = :taskName, t.taskDescription = :taskDescription, t.deadline = :deadline where t.id = :id and t.user.id = :idUser")
    public void updateTask(@Param("id") Integer id, @Param("taskName") String taskName, @Param("taskDescription") String taskDescription, @Param("deadline") Date deadline, @Param("idUser") Integer idUser);

    /**
     * Update the task status
     *
     * @param id     - task id
     * @param idUser - user id
     * @param status - new status
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE Task t set t.status = :status where t.id = :id and t.user.id = :idUser")
    public void updateStatusTask(@Param("id") Integer id, @Param("status") Integer status, @Param("idUser") Integer idUser);

    /**
     * Delete the task from the database
     *
     * @param id     - task id
     * @param idUser - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK WHERE ID = :id AND ID_USER = :idUser")
    public void deleteTask(@Param("id") Integer id, @Param("idUser") Integer idUser);

    /**
     * Delete the tasks from the database
     *
     * @param idsTasks - tasks identifiers
     * @param idUser   - user id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM TASK WHERE ID IN (:idsTasks) AND ID_USER = :idUser")
    public void deleteTasks(@Param("idsTasks") List<Integer> idsTasks, @Param("idUser") Integer idUser);

}
