package com.renan.todo.service;

import com.renan.todo.dto.TaskListDTO;
import com.renan.todo.model.TaskList;

import java.util.List;

/**
 * Services for taskLists operation
 */
public interface TaskListService {

    /**
     * Get the tasks and lists for the user
     *
     * @param userID - user id on database, get from jwt
     *
     * @return - list of tasks and taskLists
     * @throws BusinessException - in case of invalid data or other errors
     */
    public List<TaskListDTO> getTasksAndLists(Integer userID) throws BusinessException;

    /**
     * Get the lists for the user
     *
     * @param userID - user id on database, get from jwt
     *
     * @return - list of taskLists
     * @throws BusinessException - in case of invalid data or other errors
     */
    public List<TaskListDTO> getLists(Integer userID) throws BusinessException;

    /**
     * Create a new list for the user
     *
     * @param listName        - list name
     * @param listDescription - description
     * @param userID          - user id
     *
     * @return - the new list
     * @throws BusinessException - in case of invalid data or other errors
     */
    public TaskListDTO createList(String listName, String listDescription, Integer userID) throws BusinessException;

    /**
     * Create a default list for a new user on the system
     *
     * @param userID - user id
     *
     * @throws BusinessException - - in case of errors
     */
    public void createDefaultListForNewUser(Integer userID) throws BusinessException;

    /**
     * Update name and description for the list
     *
     * @param listName        - list name
     * @param listDescription - description
     * @param listID          - list id
     * @param userID          - user id
     *
     * @throws BusinessException - in case of invalid data or other errors
     */
    public void updateTaskList(String listName, String listDescription, Integer listID, Integer userID) throws BusinessException;

    /**
     * Delete the list and the tasks for the user
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @throws BusinessException - in case of invalid data or other errors
     */
    public void deleteTaskList(Integer listID, Integer userID) throws BusinessException;


}