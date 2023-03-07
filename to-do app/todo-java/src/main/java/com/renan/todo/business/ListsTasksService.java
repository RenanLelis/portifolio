package com.renan.todo.business;

import com.renan.todo.dto.TaskDTO;
import com.renan.todo.dto.TaskListDTO;

import java.util.Date;
import java.util.List;

/**
 * Service for operations on tasks and tasks lists
 */
public interface ListsTasksService {

    /**
     * Fetch the lists and tasks for the user
     *
     * @param idUser - user id
     * @return - the lists and tasks for the user
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public List<TaskListDTO> getTasksAndLists(Integer idUser) throws BusinessException;

    /**
     * Fetch the tasks for the user and list informed, validating if the list is from that user
     *
     * @param idList - list id
     * @param idUser - user id
     * @return - the list and tasks for the list
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public TaskListDTO getTasksByList(Integer idList, Integer idUser) throws BusinessException;

    /**
     * Fetch the tasks for the user that do not have a list
     *
     * @param idUser - user id
     * @return - the tasks for the user
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public List<TaskDTO> getTasksUserWithoutList(Integer idUser) throws BusinessException;

    /**
     * Fetch the lists for the user, not the tasks
     *
     * @param idUser - user id
     * @return - the lists for the user
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public List<TaskListDTO> getLists(Integer idUser) throws BusinessException;

    /**
     * Create a new List for the user
     *
     * @param listName        - the name of the list
     * @param listDescription - list description
     * @param idUser          - user id
     * @return - the new List
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public TaskListDTO createTaskList(String listName, String listDescription, Integer idUser) throws BusinessException;

    /**
     * Update the list on the database, validating if the list belongs to the user
     *
     * @param listName        - the name of the list
     * @param listDescription - list description
     * @param idList          - taskList id
     * @param idUser          - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void updateTaskList(String listName, String listDescription, Integer idList, Integer idUser) throws BusinessException;

    /**
     * Delete the taskList and all the tasks, validating if the list belongs to the user
     *
     * @param idList - list id
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void deleteTaskList(Integer idList, Integer idUser) throws BusinessException;

    /**
     * Delete the tasks from the list, validating if the list belongs to the user
     *
     * @param idList - list id
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void deleteTasksFromList(Integer idList, Integer idUser) throws BusinessException;

    /**
     * Move tasks from a List to another
     *
     * @param idOldList - old list id
     * @param idNewList - new list id
     * @param idUser    - user id
     * @param tasks     - the tasks ids
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void moveTasksForList(Integer idOldList, Integer idNewList, Integer idUser, List<Integer> tasks) throws BusinessException;

    /**
     * Create a new task for the user on the list
     *
     * @param taskName        - task name
     * @param taskDescription - task description
     * @param deadline        - deadline
     * @param idList          - list id, can be null
     * @param idUser          - user id
     * @return - the new created task
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public TaskDTO createTask(String taskName, String taskDescription, Date deadline, Integer idList, Integer idUser) throws BusinessException;

    /**
     * Update the information about the task
     *
     * @param id              - task id
     * @param taskName        - task name
     * @param taskDescription - task description
     * @param deadline        - task deadline
     * @param idUser          - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void updateTask(Integer id, String taskName, String taskDescription, Date deadline, Integer idUser) throws BusinessException;

    /**
     * Delete the task from the database
     *
     * @param id     - task id
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void deleteTask(Integer id, Integer idUser) throws BusinessException;

    /**
     * Delete the tasks from the database
     *
     * @param idsTasks - task ids
     * @param idUser   - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void deleteTasks(List<Integer> idsTasks, Integer idUser) throws BusinessException;

    /**
     * Marks the task as complete
     *
     * @param id     - task ids
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void completeTask(Integer id, Integer idUser) throws BusinessException;

    /**
     * Marks the task as incomplete
     *
     * @param id     - task ids
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void unCompleteTask(Integer id, Integer idUser) throws BusinessException;

}
