package com.renan.todo.persistence;

import com.renan.todo.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for lists operations
 */
public interface TaskListRepository extends JpaRepository<TaskList, Integer> {

    /**
     * Fetch the task lists for the user
     *
     * @param userId - user id
     * @return - the task lists
     */
    @Query(value = "SELECT tl.id u set u.newPasswordCode = :newPasswordCode WHERE u.email = :email")
    public List<TaskList> findByUser(Integer userId);

}
