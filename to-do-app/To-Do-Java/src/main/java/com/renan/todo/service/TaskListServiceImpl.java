package com.renan.todo.service;

import com.renan.todo.dto.TaskListDTO;
import com.renan.todo.dto.TaskListDTOMapper;
import com.renan.todo.model.Task;
import com.renan.todo.model.TaskList;
import com.renan.todo.model.User;
import com.renan.todo.persistence.TaskListRepository;
import com.renan.todo.persistence.TaskRepository;
import com.renan.todo.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Services for taskLists operation
 */
@Service
@RequiredArgsConstructor
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final TaskListDTOMapper taskListDTOMapper;
    private final UtilService utilService;

    /**
     * Get the tasks and lists for the user
     *
     * @param userID - user id on database, get from jwt
     *
     * @return - list of tasks and taskLists
     * @throws BusinessException - in case of invalid data or other errors
     */
    public List<TaskListDTO> getTasksAndLists(Integer userID) throws BusinessException {
        if (userID == null || userID <= 0)
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            List<TaskList> lists = taskListRepository.getListsByUser(userID);
            List<Task> tasks = taskRepository.getTasksByUser(userID);
            return taskListDTOMapper.convertMultipleListsAndTasks(lists, tasks);
        } catch (Exception e) {
            handleException(e);
        }
        return new ArrayList<>();
    }

    /**
     * Get the lists for the user
     *
     * @param userID - user id on database, get from jwt
     *
     * @return - list of taskLists
     * @throws BusinessException - in case of invalid data or other errors
     */
    public List<TaskListDTO> getLists(Integer userID) throws BusinessException {
        if (userID == null || userID <= 0)
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            List<TaskList> lists = taskListRepository.getListsByUser(userID);
            return lists.stream().map(taskListDTOMapper).collect(Collectors.toList());
        } catch (Exception e) {
            handleException(e);
        }
        return new ArrayList<>();
    }

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
    public TaskListDTO createList(String listName, String listDescription, Integer userID) throws BusinessException {
        if (!validateNewListInput(listName, userID))
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        User user = new User();
        user.setId(userID);
        TaskList newList = new TaskList();
        newList.setListName(listName.trim());
        newList.setUser(user);
        newList.setListDescription(listDescription != null ? listDescription.trim() : null);
        return taskListDTOMapper.apply(taskListRepository.save(newList));
    }

    /**
     * Validate input data for new list creation
     *
     * @param listName - list name
     * @param userID   - user id
     *
     * @return - true if is valid
     */
    private boolean validateNewListInput(String listName, Integer userID) {
        return utilService.isNotEmptyString(listName)
                && userID != null && userID > 0;
    }

    /**
     * Create a default list for a new user on the system
     *
     * @param userID - user id
     *
     * @throws BusinessException - - in case of errors
     */
    public void createDefaultListForNewUser(Integer userID) throws BusinessException {
        createList(
                "My Tasks",
                "Default list for the user, created by the app",
                userID
        );
    }

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
    public void updateTaskList(String listName, String listDescription, Integer listID
            , Integer userID) throws BusinessException {
        if (!validateUpdateListInput(listName, listID, userID))
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            taskListRepository.updateTaskList(listName, listDescription, listID, userID);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Validate input data for new list creation
     *
     * @param listName - list name
     * @param listID   - list id
     * @param userID   - user id
     *
     * @return - true if is valid
     */
    private boolean validateUpdateListInput(String listName, Integer listID, Integer userID) {
        return utilService.isNotEmptyString(listName)
                && userID != null && userID > 0
                && listID != null && listID > 0;
    }

    /**
     * Delete the list and the tasks for the user
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @throws BusinessException - in case of invalid data or other errors
     */
    public void deleteTaskList(Integer listID, Integer userID) throws BusinessException {
        try {
            List<TaskList> lists = taskListRepository.getListsByUser(userID);
            if (lists == null || lists.size() == 0) {
                throw new BusinessException(
                        MessageUtil.getErrorMessageInputValues(),
                        BusinessException.BUSINESS_MESSAGE,
                        AppErrorType.INVALID_INPUT
                );
            }
            taskService.deleteTasksFromList(listID, userID);
            taskListRepository.deleteTaskList(listID, userID);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * handle exceptions for the operations
     *
     * @param e - exception
     *
     * @throws BusinessException - throw the Exception with the message for the client
     */
    private void handleException(Exception e) throws BusinessException {
        e.printStackTrace();
        if (e instanceof BusinessException)
            throw (BusinessException) e;
        else
            throw new BusinessException(MessageUtil.getErrorMessage()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INTERN_ERROR);
    }

}