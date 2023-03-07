package com.renan.todo.persistence;

import com.renan.todo.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

}
