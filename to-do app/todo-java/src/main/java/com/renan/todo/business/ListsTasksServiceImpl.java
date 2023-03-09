package com.renan.todo.business;

import com.renan.todo.dto.TaskDTO;
import com.renan.todo.dto.TaskDTOMapper;
import com.renan.todo.dto.TaskListDTO;
import com.renan.todo.dto.TaskListDTOMapper;
import com.renan.todo.entities.Task;
import com.renan.todo.entities.TaskList;
import com.renan.todo.entities.User;
import com.renan.todo.persistence.TaskListRepository;
import com.renan.todo.persistence.TaskRepository;
import com.renan.todo.persistence.UserRepository;
import com.renan.todo.util.MessageUtil;
import com.renan.todo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service for operations on tasks and tasks lists
 */
@Service
public class ListsTasksServiceImpl implements ListsTasksService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskDTOMapper taskDTOMapper;
    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private TaskListDTOMapper taskListDTOMapper;

    /**
     * Fetch the lists and tasks for the user
     *
     * @param idUser - user id
     * @return - the lists and tasks for the user
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public List<TaskListDTO> getTasksAndLists(Integer idUser) throws BusinessException {
        List<Task> tasks = taskRepository.findTaskByUser(idUser);
        return taskListDTOMapper.convertMultipleListsAndTasks(tasks);
    }

    /**
     * Create a new List for the user
     *
     * @param listName        - the name of the list
     * @param listDescription - list description
     * @param idUser          - user id
     * @return - the new List
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public TaskListDTO createTaskList(String listName, String listDescription, Integer idUser) throws BusinessException {
        if (!validateInputTaskList(listName, idUser)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty() || user.get() == null) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        return taskListDTOMapper.apply(taskListRepository.save(new TaskList(null, listName, listDescription, user.get())));
    }

    /**
     * Update the list on the database, validating if the list belongs to the user
     *
     * @param listName        - the name of the list
     * @param listDescription - list description
     * @param idList          - taskList id
     * @param idUser          - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void updateTaskList(String listName, String listDescription, Integer idList, Integer idUser) throws BusinessException {
        if (!validateInputTaskList(listName, idUser)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        taskListRepository.updateTaskList(listName, listDescription, idList, idUser);
    }

    /**
     * Delete the taskList and all the tasks, validating if the list belongs to the user
     *
     * @param idList - list id
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void deleteTaskList(Integer idList, Integer idUser) throws BusinessException {
        if (idList == null || idList <= 0 || idUser == null || idUser <= 0) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        deleteTasksFromList(idList, idUser);
        taskListRepository.deleteTaskList(idList, idUser);
    }

    /**
     * Delete the tasks from the list, validating if the list belongs to the user
     *
     * @param idList - list id
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void deleteTasksFromList(Integer idList, Integer idUser) throws BusinessException {
        if (idUser == null || idUser <= 0) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        if (idList == null || idList <= 0) {
            taskRepository.deleteTasksWithoutList(idUser);
        } else {
            taskRepository.deleteTasksFromList(idList, idUser);
        }
    }

    /**
     * Move tasks from a List to another
     *
     * @param idOldList - old list id
     * @param idNewList - new list id
     * @param idUser    - user id
     * @param tasks     - the tasks ids
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void moveTasksForList(Integer idOldList, Integer idNewList, Integer idUser, List<Integer> tasks) throws BusinessException {
        if (idOldList == null || idOldList <= 0 || idUser == null || idUser <= 0) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        if (idNewList == null || idNewList <= 0) taskRepository.moveTasksToDefaultList(idOldList, idUser);
        else taskRepository.moveTasksToAnotherList(idOldList, idNewList, idUser);
    }

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
    public TaskDTO createTask(String taskName, String taskDescription, Date deadline, Integer idList, Integer idUser) throws BusinessException {
        if (!validateInputTask(taskName, idUser)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty() || user.get() == null) {
            throw new BusinessException(MessageUtil.getErrorMessageUserNotFound(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        Optional<TaskList> taskList = taskListRepository.findById(idList);
        if (taskList.isEmpty() || taskList.get() == null) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        Task task = new Task(
                null,
                taskName,
                taskDescription != null && taskDescription.length() > 0 ? taskDescription : null,
                deadline,
                Task.STATUS_COMPLETE,
                user.get(),
                taskList.get()
        );
        return taskDTOMapper.apply(taskRepository.save(task));
    }

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
    public void updateTask(Integer id, String taskName, String taskDescription, Date deadline, Integer idUser) throws BusinessException {
        if (id == null || id <= 0 || !validateInputTask(taskName, idUser)) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        taskRepository.updateTask(id, taskName, taskDescription, deadline, idUser);
    }

    /**
     * Delete the task from the database
     *
     * @param id     - task id
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void deleteTask(Integer id, Integer idUser) throws BusinessException {
        if (id == null || id <= 0 || idUser == null || idUser <= 0) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        taskRepository.deleteTask(id, idUser);
    }

    /**
     * Delete the tasks from the database
     *
     * @param idsTasks - task ids
     * @param idUser   - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void deleteTasks(List<Integer> idsTasks, Integer idUser) throws BusinessException {
        if (idsTasks == null || idsTasks.size() <= 0 || idUser == null || idUser <= 0) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        taskRepository.deleteTasks(idsTasks, idUser);
    }

    /**
     * Marks the task as complete
     *
     * @param id     - task ids
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void completeTask(Integer id, Integer idUser) throws BusinessException {
        changeTaskStatus(id, idUser, Task.STATUS_COMPLETE);
    }

    /**
     * Marks the task as incomplete
     *
     * @param id     - task id
     * @param idUser - user id
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    public void unCompleteTask(Integer id, Integer idUser) throws BusinessException {
        changeTaskStatus(id, idUser, Task.STATUS_INCOMPLETE);
    }

    /**
     * Marks the task as the status
     *
     * @param id     - task id
     * @param idUser - user id
     * @param status - new status
     * @throws BusinessException - in cases of any errors or invalid operations
     */
    private void changeTaskStatus(Integer id, Integer idUser, Integer status) throws BusinessException {
        if (id == null || id <= 0 || idUser == null || idUser <= 0) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        taskRepository.updateStatusTask(id, status, idUser);
    }

    /**
     * Validate user input for create or update task list
     *
     * @param listName - list name
     * @param idUser   - user id
     * @return - true if all data is valid
     */
    private boolean validateInputTaskList(String listName, Integer idUser) {
        return idUser != null && idUser > 0 && StringUtil.isNotEmpty(listName);
    }

    /**
     * Validate the input data for creation or update task
     *
     * @param taskName - task name
     * @param idUser   - user id
     * @return - true if is valid
     */
    private boolean validateInputTask(String taskName, Integer idUser) {
        return idUser != null && idUser > 0 && StringUtil.isNotEmpty(taskName);
    }

}
