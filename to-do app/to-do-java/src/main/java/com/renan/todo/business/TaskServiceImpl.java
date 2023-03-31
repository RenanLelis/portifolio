package com.renan.todo.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renan.todo.dto.TaskDTO;
import com.renan.todo.dto.TaskDTOMapper;
import com.renan.todo.dto.TaskListDTO;
import com.renan.todo.dto.TaskListDTOMapper;
import com.renan.todo.model.Task;
import com.renan.todo.model.TaskList;
import com.renan.todo.model.User;
import com.renan.todo.persistence.TaskListRepository;
import com.renan.todo.persistence.TaskRepository;
import com.renan.todo.util.MessageUtil;
import com.renan.todo.util.StringUtil;

/**
 * Services for tasks and lists operations
 */
@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskListRepository taskListRepository;
	@Autowired
	private TaskRepository     taskRepository;
//	@Autowired
//	private UserRepository     userRepository;
	@Autowired
	private TaskListDTOMapper taskListDTOMapper;
	@Autowired
	private TaskDTOMapper     taskDTOMapper;

	/**
	 * Fetch the lists and tasks for the user
	 *
	 * @param idUser - user id
	 * @return - the lists and tasks for the user
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	public List<TaskListDTO> getTasksAndLists(Integer idUser) throws BusinessException {
		List<TaskList>    lists  = taskListRepository.findByUser(idUser);
		List<Task>        tasks  = taskRepository.findTaskByUser(idUser);
		List<TaskListDTO> result = taskListDTOMapper.convertMultipleListsAndTasks(lists, tasks);
		return result;
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
	public TaskListDTO createTaskList(String listName, String listDescription, Integer idUser)
	        throws BusinessException {
		validateTaskListData(listName, listDescription, idUser);
		TaskList taskList = taskListRepository.save(new TaskList(null, listName, listDescription,
		        new User(idUser, null, null, null, null, null, null, null)));
//		User u = userRepository.findById(idUser).get();
//		TaskList taskList = taskListRepository.save(new TaskList(null, listName, listDescription, u));
		return taskListDTOMapper.apply(taskList);
	}

	/**
	 * Validate the params to create a task list
	 * 
	 * @param listName        - list name
	 * @param listDescription - list description
	 * @param idUser          - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	private void validateTaskListData(String listName, String listDescription, Integer idUser)
	        throws BusinessException {
		if (StringUtil.isEmpty(listName) || idUser == null || idUser <= 0) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
	}

	/**
	 * Validate the params to update a task list
	 * 
	 * @param listName        - list name
	 * @param listDescription - list description
	 * @param idUser          - user id
	 * @param listId          - list id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	private void validateTaskListData(String listName, String listDescription, Integer idUser, Integer listId)
	        throws BusinessException {
		if (StringUtil.isEmpty(listName) || idUser == null || idUser <= 0 || listId == null || listId <= 0) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
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
	public void updateTaskList(String listName, String listDescription, Integer idList, Integer idUser)
	        throws BusinessException {
		validateTaskListData(listName, listDescription, idUser, idList);
		taskListRepository.updateTaskList(listName, listDescription, idList, idUser);
	}

	/**
	 * Delete the taskList and all the tasks, validating if the list belongs to the
	 * user
	 *
	 * @param idList - list id
	 * @param idUser - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	public void deleteTaskList(Integer idList, Integer idUser) throws BusinessException {
		if (idUser == null || idUser <= 0 || idList == null || idList <= 0) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
		taskRepository.deleteTasksFromList(idList, idUser);
		taskListRepository.deleteTaskList(idList, idUser);
	}

	/**
	 * Complete all the tasks on the task, validating if the list belongs to the
	 * user
	 *
	 * @param idList - list id
	 * @param idUser - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	public void completeTaskList(Integer idList, Integer idUser) throws BusinessException {
		if (idUser == null || idUser <= 0 || idList == null || idList <= 0) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
		taskRepository.UpdateStatusTasksFromList(idList, Task.STATUS_COMPLETE, idUser);
	}

	/**
	 * Uncomplete all the tasks on the task, validating if the list belongs to the
	 * user
	 *
	 * @param idList - list id
	 * @param idUser - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	public void uncompleteTaskList(Integer idList, Integer idUser) throws BusinessException {
		if (idUser == null || idUser <= 0 || idList == null || idList <= 0) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
		taskRepository.UpdateStatusTasksFromList(idList, Task.STATUS_INCOMPLETE, idUser);
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
	public TaskDTO createTask(String taskName, String taskDescription, String deadline, Integer idList, Integer idUser)
	        throws BusinessException {
		validateTaskData(taskName, taskDescription, deadline, idList, idUser);
		Task task = taskRepository.save(new Task(null, taskName, taskDescription, deadline, Task.STATUS_INCOMPLETE,
		        new User(idUser, null, null, null, null, null, null, null), new TaskList(idList, null, null, null)));
		return taskDTOMapper.apply(task);
	}

	/**
	 * Validate task input data for new task creation
	 * 
	 * @param taskName        - task name
	 * @param taskDescription - task description
	 * @param deadline        - task deadline
	 * @param idList          - list id
	 * @param idUser          - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	private void validateTaskData(String taskName, String taskDescription, String deadline, Integer idList,
	        Integer idUser) throws BusinessException {
		if (StringUtil.isEmpty(taskName) || idUser == null || idUser <= 0 || !isDeadlineInValidFormat(deadline)) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
	}

	/**
	 * check if the deadline is in a valid format
	 * 
	 * @param deadline - task deadline
	 * @return - true is is valid
	 */
	private boolean isDeadlineInValidFormat(String deadline) {
		return StringUtil.isValidDate(deadline);
	}

	/**
	 * Validate task input data for task update
	 * 
	 * @param id              - task id
	 * @param taskName        - task name
	 * @param taskDescription - task description
	 * @param deadline        - task deadline
	 * @param idList          - list id
	 * @param idUser          - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	private void validateTaskData(Integer id, String taskName, String taskDescription, String deadline,
	        Integer idUser) {
		if (id == null || id <= 0 || StringUtil.isEmpty(taskName) || idUser == null || idUser <= 0
		        || !isDeadlineInValidFormat(deadline)) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
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
	public void updateTask(Integer id, String taskName, String taskDescription, String deadline, Integer idUser)
	        throws BusinessException {
		validateTaskData(id, taskName, taskDescription, deadline, idUser);
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
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
		taskRepository.deleteTask(id, idUser);
	}

	/**
	 * Marks the task as complete
	 *
	 * @param id     - task ids
	 * @param idUser - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	public void completeTask(Integer id, Integer idUser) throws BusinessException {
		if (id == null || id <= 0 || idUser == null || idUser <= 0) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
		taskRepository.updateStatusTask(id, Task.STATUS_COMPLETE, idUser);
	}

	/**
	 * Marks the task as incomplete
	 *
	 * @param id     - task ids
	 * @param idUser - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	public void unCompleteTask(Integer id, Integer idUser) throws BusinessException {
		if (id == null || id <= 0 || idUser == null || idUser <= 0) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
		taskRepository.updateStatusTask(id, Task.STATUS_INCOMPLETE, idUser);
	}

	/**
	 * Move tasks from a List to another
	 *
	 * @param id        - the task id
	 * @param idNewList - new list id
	 * @param idUser    - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	public void moveTaskForList(Integer id, Integer idNewList, Integer idUser) throws BusinessException {
		if (id == null || id <= 0 || idUser == null || idUser <= 0) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
		taskRepository.moveTaskForList(id, idNewList != null && idNewList <= 0 ? null : idNewList, idUser);
	}

	/**
	 * Move tasks from a List to another
	 *
	 * @param oldList   - old list id
	 * @param idNewList - new list id
	 * @param idUser    - user id
	 * @throws BusinessException - in cases of any errors or invalid operations
	 */
	public void moveTaskFromAListToAnother(Integer oldList, Integer idNewList, Integer idUser)
	        throws BusinessException {
		if (idUser == null || idUser <= 0) {
			throw new BusinessException(MessageUtil.getErrorMessageInputValues(), BusinessException.BUSINESS_MESSAGE,
			        AppErrorType.INVALID_INPUT);
		}
		if (idNewList == null || idNewList <= 0)
			taskRepository.moveTasksToDefaultList(oldList != null && oldList <= 0 ? null : oldList, idUser);
		else
			taskRepository.moveTasksToAnotherList(oldList != null && oldList <= 0 ? null : oldList, idNewList, idUser);
	}

}
