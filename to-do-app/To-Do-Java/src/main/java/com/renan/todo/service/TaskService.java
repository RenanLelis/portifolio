package com.renan.todo.service;


import com.renan.todo.dto.TaskDTO;

import java.util.List;

/**
 * Services for tasks operation
 */
public interface TaskService {

    /**
     * Get the tasks for the list passed
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @return - list of tasks for the list id and user id
     * @throws BusinessException - for invalid user, list or other errors
     */
    public List<TaskDTO> getTasksByList(Integer listID, Integer userID) throws BusinessException;

    /**
     * Delete all tasks from the list
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid user, list or other errors
     */
    public void deleteTasksFromList(Integer listID, Integer userID) throws BusinessException;

    /**
     * Mark as complete all tasks from the list
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid user, list or other errors
     */
    public void completeTasksFromList(Integer listID, Integer userID) throws BusinessException;

    /**
     * Mark as incomplete all tasks from the list
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid user, list or other errors
     */
    public void incompleteTasksFromList(Integer listID, Integer userID) throws BusinessException;

    /**
     * Change the listID on all tasks from a list
     *
     * @param listIDOrigin  - list id from where the tasks will be updated
     * @param listIDDestiny - list id to set to the tasks
     * @param userID        - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void moveTasksFromList(Integer listIDOrigin,
                                  Integer listIDDestiny, Integer userID) throws BusinessException;

    /**
     * Create a new task for the user
     *
     * @param taskName        - task name
     * @param taskDescription - description
     * @param deadline        - deadline
     * @param listID          - list id (the list where will be saved the new task)
     * @param userID          - user id
     *
     * @return - the new task created
     * @throws BusinessException - for invalid input, list or other errors
     */
    public TaskDTO createTask(String taskName, String taskDescription, String deadline
            , Integer listID, Integer userID) throws BusinessException;

    /**
     * Update a task on the database
     *
     * @param taskName        - task name
     * @param taskDescription - description
     * @param deadline        - deadline
     * @param taskID          - task id
     * @param userID          - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void updateTask(String taskName, String taskDescription, String deadline
            , Integer taskID, Integer userID) throws BusinessException;

    /**
     * change the list for the task
     *
     * @param taskID        - task id
     * @param listIDDestiny - new list for the task
     * @param userID        - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void moveTaskToList(Integer taskID, Integer listIDDestiny, Integer userID) throws BusinessException;

    /**
     * Delete the task on the database
     *
     * @param taskID - task id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void deleteTask(Integer taskID, Integer userID) throws BusinessException;

    /**
     * Mark the task as complete
     *
     * @param taskID - task id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void completeTask(Integer taskID, Integer userID) throws BusinessException;

    /**
     * Mark the task as incomplete
     *
     * @param taskID - task id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void incompleteTask(Integer taskID, Integer userID) throws BusinessException;

}