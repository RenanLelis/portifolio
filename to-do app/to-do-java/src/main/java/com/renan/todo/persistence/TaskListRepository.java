package com.renan.todo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.renan.todo.model.TaskList;

import jakarta.transaction.Transactional;

/**
 * Repository for lists operations
 */
public interface TaskListRepository extends JpaRepository<TaskList, Integer> {

	/**
	 * Fetch the task lists for the user
	 *
	 * @param idUser - user id
	 * @return - the task lists
	 */
//    @Query(nativeQuery = true, value = "SELECT ID, LIST_NAME, LIST_DESCRIPTION FROM TASK_LIST WHERE ID_USER = :idUser")
	@Query(value = "SELECT tl.id, tl.listName, tl.listDescription FROM TaskList tl WHERE tl.user.id = :idUser")
	public List<TaskList> findByUser(@Param("idUser") Integer idUser);

	/**
	 * Update the information about the list on the database
	 *
	 * @param listName        - list name
	 * @param listDescription - list description
	 * @param idList          - list id
	 * @param idUser          - user id
	 */
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE TASK_LIST SET LIST_NAME = :listName, LIST_DESCRIPTION = :listDescription WHERE ID = :idList AND ID_USER = :idUser")
	public void updateTaskList(@Param("listName") String listName, @Param("listDescription") String listDescription,
	        @Param("idList") Integer idList, @Param("idUser") Integer idUser);

	/**
	 * Delete the list from the database
	 *
	 * @param idList - list id
	 * @param idUser - user id
	 */
	@Transactional
	@Modifying
	// @Query(nativeQuery = true, value = "DELETE t.*, tl.* FROM TASK t LEFT JOIN
	// TASK_LIST tl ON tl.ID = t.ID_LIST WHERE tl.ID = :idList AND tl.ID_USER =
	// :idUser")
	@Query(nativeQuery = true, value = "DELETE FROM TASK_LIST WHERE ID = :idList AND ID_USER = :idUser")
	public void deleteTaskList(@Param("idList") Integer idList, @Param("idUser") Integer idUser);
}
