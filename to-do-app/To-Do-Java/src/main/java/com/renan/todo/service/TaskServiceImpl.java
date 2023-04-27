package com.renan.todo.service;

import com.renan.todo.dto.TaskDTO;
import com.renan.todo.dto.TaskDTOMapper;
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
 * Services for tasks operation
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final TaskDTOMapper taskDTOMapper;
    private final UtilService utilService;

    /**
     * Get the tasks for the list passed
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @return - list of tasks for the list id and user id
     * @throws BusinessException - for invalid user, list or other errors
     */
    public List<TaskDTO> getTasksByList(Integer listID, Integer userID) throws BusinessException {
        if (listID == null || listID <= 0 || userID == null || userID <= 0)
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            List<Task> tasks = taskRepository.getTasksByList(listID, userID);
            if (tasks == null || tasks.size() == 0) {
                return new ArrayList<>();
            }
            return tasks.stream().map(taskDTOMapper).collect(Collectors.toList());
        } catch (Exception e) {
            handleException(e);
        }
        return new ArrayList<>();
    }

    /**
     * Delete all tasks from the list
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid user, list or other errors
     */
    public void deleteTasksFromList(Integer listID, Integer userID) throws BusinessException {
        if (listID == null || listID <= 0 || userID == null || userID <= 0)
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            taskRepository.deleteTasksFromList(listID, userID);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Mark as complete all tasks from the list
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid user, list or other errors
     */
    public void completeTasksFromList(Integer listID, Integer userID) throws BusinessException {
        this.updateStatusTasksFromList(listID, userID, Task.STATUS_COMPLETE);
    }

    /**
     * Mark as incomplete all tasks from the list
     *
     * @param listID - list id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid input or other errors
     */
    public void incompleteTasksFromList(Integer listID, Integer userID) throws BusinessException {
        this.updateStatusTasksFromList(listID, userID, Task.STATUS_INCOMPLETE);
    }

    /**
     * Update the status for all tasks from a list
     *
     * @param listID - list id
     * @param userID - user id
     * @param status - new status
     *
     * @throws BusinessException - for invalid user, list or other errors
     */
    private void updateStatusTasksFromList(Integer listID, Integer userID, Integer status) throws BusinessException {
        if (listID == null || listID <= 0 || status == null || status <= 0 || userID == null || userID <= 0)
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            taskRepository.updateStatusTasksFromList(listID, userID, status);
        } catch (Exception e) {
            handleException(e);
        }
    }

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
                                  Integer listIDDestiny, Integer userID) throws BusinessException {
        if (listIDOrigin == null || listIDOrigin <= 0 || listIDDestiny == null || listIDDestiny <= 0
                || userID == null || userID <= 0)
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            taskRepository.moveTasksFromList(listIDOrigin, listIDDestiny, userID);
        } catch (Exception e) {
            handleException(e);
        }
    }

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
            , Integer listID, Integer userID) throws BusinessException {
        if (!validateNewTaskInput(taskName, deadline, listID, userID))
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            User user = new User();
            user.setId(userID);
            TaskList list = new TaskList();
            list.setId(listID);
            Task newTask = new Task(
                    null,
                    taskName.trim(),
                    taskDescription != null ? taskDescription.trim() : null,
                    deadline != null ? deadline.trim() : null,
                    Task.STATUS_INCOMPLETE,
                    user,
                    list
            );
            return taskDTOMapper.apply(taskRepository.save(newTask));
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    /**
     * Validate the input data for new task
     *
     * @param taskName        - name
     * @param deadline        - task deadline
     * @param listID          - list id
     * @param userID          - user id
     *
     * @return - true if data is valid
     */
    private boolean validateNewTaskInput(String taskName, String deadline
            , Integer listID, Integer userID) {
        return listID != null && listID > 0 && userID != null && userID > 0
                && utilService.isNotEmptyString(taskName)
                && isValidDeadline(deadline);
    }

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
            , Integer taskID, Integer userID) throws BusinessException {
        if (!validateUpdateTaskInput(taskName, deadline, taskID, userID))
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);

        try {
            taskRepository.updateTask(taskName.trim(),
                    taskDescription != null ? taskDescription.trim() : null,
                    deadline != null ? deadline.trim() : null,
                    taskID, userID);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Validate the input data for new task
     *
     * @param taskName        - name
     * @param deadline        - task deadline
     * @param taskID          - task id
     * @param userID          - user id
     *
     * @return - true if data is valid
     */
    private boolean validateUpdateTaskInput(String taskName, String deadline
            , Integer taskID, Integer userID) {
        return taskID != null && taskID > 0 && userID != null && userID > 0
                && utilService.isNotEmptyString(taskName)
                && isValidDeadline(deadline);
    }

    /**
     * Validate string deadline
     *
     * @param deadline - input to be validated
     *
     * @return - true if is valid
     */
    private boolean isValidDeadline(String deadline) {
        if (utilService.isEmptyString(deadline)) return true;
        return utilService.isValidDate(deadline);
    }

    /**
     * change the list for the task
     *
     * @param taskID        - task id
     * @param listIDDestiny - new list for the task
     * @param userID        - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void moveTaskToList(Integer taskID, Integer listIDDestiny, Integer userID) throws BusinessException {
        if (taskID == null || taskID <= 0 || userID == null || userID <= 0 || listIDDestiny == null || listIDDestiny <= 0)
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            TaskList list = taskListRepository.getListByIdAndUser(listIDDestiny, userID);
            if (list == null) {
                throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                        , BusinessException.BUSINESS_MESSAGE
                        , AppErrorType.INVALID_INPUT);
            }
            taskRepository.moveTaskToList(taskID, listIDDestiny, userID);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Delete the task on the database
     *
     * @param taskID - task id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void deleteTask(Integer taskID, Integer userID) throws BusinessException {
        if (taskID == null || taskID <= 0 || userID == null || userID <= 0)
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            taskRepository.deleteTask(taskID, userID);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Mark the task as complete
     *
     * @param taskID - task id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void completeTask(Integer taskID, Integer userID) throws BusinessException {
        this.updateStatusTask(taskID, userID, Task.STATUS_COMPLETE);
    }

    /**
     * Update the status for a task
     *
     * @param taskID - task id
     * @param userID - user id
     * @param status - new status
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    private void updateStatusTask(Integer taskID, Integer userID, Integer status) throws BusinessException {
        if (taskID == null || taskID <= 0 || userID == null || userID <= 0)
            throw new BusinessException(MessageUtil.getErrorMessageInputValues()
                    , BusinessException.BUSINESS_MESSAGE
                    , AppErrorType.INVALID_INPUT);
        try {
            taskRepository.updateTaskStatus(taskID, userID, status);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Mark the task as incomplete
     *
     * @param taskID - task id
     * @param userID - user id
     *
     * @throws BusinessException - for invalid input, list or other errors
     */
    public void incompleteTask(Integer taskID, Integer userID) throws BusinessException {
        this.updateStatusTask(taskID, userID, Task.STATUS_INCOMPLETE);
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