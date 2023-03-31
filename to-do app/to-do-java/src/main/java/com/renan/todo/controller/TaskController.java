package com.renan.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.renan.todo.business.BusinessException;
import com.renan.todo.business.TaskService;
import com.renan.todo.controller.filter.AuthFilter;
import com.renan.todo.controller.form.MoveTaskToListForm;
import com.renan.todo.controller.form.MoveTasksFromListToListForm;
import com.renan.todo.controller.form.TaskForm;
import com.renan.todo.controller.form.TaskListForm;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.dto.TaskDTO;
import com.renan.todo.dto.TaskListDTO;
import com.renan.todo.util.JwtUtil;
import com.renan.todo.util.MessageUtil;

/**
 * The controller for tasks and lists operations
 */
@RestController()
@SuppressWarnings("rawtypes")
public class TaskController {

	@Autowired
	private TaskService taskService;

	/**
	 * Fetch the lists and tasks for the user
	 * 
	 * @param jwt - the auth token from the user
	 * @return - the ResponseEntity
	 */
	@GetMapping(value = "/api/taskList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getTasksAndLists(@RequestHeader(AuthFilter.AUTH) String jwt) {
		try {
			Integer           userID = JwtUtil.getIdUser(jwt);
			List<TaskListDTO> lists  = taskService.getTasksAndLists(userID);
			return ResponseEntity.ok(lists);
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Create a new List for the user
	 * 
	 * @param jwt  - the auth token from the user
	 * @param form - form data for new task list
	 * @return - the ResponseEntity
	 */
	@PostMapping(value = "/api/taskList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createTaskList(@RequestHeader(AuthFilter.AUTH) String jwt, @RequestBody TaskListForm form) {
		try {
			if (form.getListName() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer     userID   = JwtUtil.getIdUser(jwt);
			TaskListDTO taskList = taskService.createTaskList(form.getListName().trim(),
			        form.getListDescription() != null ? form.getListDescription().trim() : null, userID);
			return ResponseEntity.ok(taskList);
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Update a TaskList
	 * 
	 * @param jwt    - the auth token of the user, to get user data
	 * @param form   - the input from the user
	 * @param idList - list identifier
	 * @return - Response Entity
	 */
	@PutMapping(value = "/api/taskList/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateTaskList(@RequestHeader(AuthFilter.AUTH) String jwt, @RequestBody TaskListForm form,
	        @PathVariable("id") Integer idList) {
		try {
			if (form.getListName() == null || idList == null || idList <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.updateTaskList(form.getListName().trim(),
			        form.getListDescription() != null ? form.getListDescription().trim() : null, idList, userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Delete a TaskList and the tasks related
	 * 
	 * @param jwt    - the auth token of the user, to get user data
	 * @param idList - list identifier
	 * @return - Response Entity
	 */
	@DeleteMapping(value = "/api/taskList/{id}")
	public ResponseEntity deleteTaskList(@RequestHeader(AuthFilter.AUTH) String jwt,
	        @PathVariable("id") Integer idList) {
		try {
			if (idList == null || idList <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.deleteTaskList(idList, userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Complete all tasks from the list passed as parameter
	 * 
	 * @param jwt    - the auth token of the user, to get user data
	 * @param idList - list identifier
	 * @return - Response Entity
	 */
	@PutMapping(value = "/api/taskList/tasks/complete/{id}")
	public ResponseEntity completeTaskList(@RequestHeader(AuthFilter.AUTH) String jwt,
	        @PathVariable("id") Integer idList) {
		try {
			if (idList == null || idList <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.completeTaskList(idList, userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Complete all tasks from the list passed as parameter
	 * 
	 * @param jwt    - the auth token of the user, to get user data
	 * @param idList - list identifier
	 * @return - Response Entity
	 */
	@PutMapping(value = "/api/taskList/tasks/uncomplete/{id}")
	public ResponseEntity uncompleteTaskList(@RequestHeader(AuthFilter.AUTH) String jwt,
	        @PathVariable("id") Integer idList) {
		try {
			if (idList == null || idList <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.uncompleteTaskList(idList, userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Create a new task for the user
	 * 
	 * @param jwt  - the auth token from the user
	 * @param form - form data for new task list
	 * @return - the ResponseEntity
	 */
	@PostMapping(value = "/api/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createTask(@RequestHeader(AuthFilter.AUTH) String jwt, @RequestBody TaskForm form) {
		try {
			if (form.getTaskName() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			TaskDTO task   = taskService.createTask(form.getTaskName().trim(),
			        form.getTaskDescription() != null ? form.getTaskDescription().trim() : null,
			        form.getDeadline() != null ? form.getDeadline().trim() : null,
			        form.getIdList() != null && form.getIdList() > 0 ? form.getIdList() : null, userID);
			return ResponseEntity.ok(task);
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Update task for the user
	 * 
	 * @param jwt    - the auth token from the user
	 * @param form   - form data for new task list
	 * @param taskID - task id
	 * @return - the ResponseEntity
	 */
	@PutMapping(value = "/api/task/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateTask(@RequestHeader(AuthFilter.AUTH) String jwt, @RequestBody TaskForm form,
	        @PathVariable("id") Integer taskID) {
		try {
			if (form.getTaskName() == null || taskID == null || taskID <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.updateTask(taskID, form.getTaskName().trim(),
			        form.getTaskDescription() != null ? form.getTaskDescription().trim() : null,
			        form.getDeadline() != null ? form.getDeadline().trim() : null, userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Delete task for the user
	 * 
	 * @param jwt    - the auth token from the user
	 * @param taskID - task id
	 * @return - the ResponseEntity
	 */
	@DeleteMapping(value = "/api/task/{id}")
	public ResponseEntity deleteTask(@RequestHeader(AuthFilter.AUTH) String jwt, @PathVariable("id") Integer taskID) {
		try {
			if (taskID == null || taskID <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.deleteTask(taskID, userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Mark task as completed for the user
	 * 
	 * @param jwt    - the auth token from the user
	 * @param taskID - task id
	 * @return - the ResponseEntity
	 */
	@PutMapping(value = "/api/task/complete/{id}")
	public ResponseEntity completeTask(@RequestHeader(AuthFilter.AUTH) String jwt, @PathVariable("id") Integer taskID) {
		try {
			if (taskID == null || taskID <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.completeTask(taskID, userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Mark task as incompleted for the user
	 * 
	 * @param jwt    - the auth token from the user
	 * @param taskID - task id
	 * @return - the ResponseEntity
	 */
	@PutMapping(value = "/api/task/uncomplete/{id}")
	public ResponseEntity unCompleteTask(@RequestHeader(AuthFilter.AUTH) String jwt,
	        @PathVariable("id") Integer taskID) {
		try {
			if (taskID == null || taskID <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.unCompleteTask(taskID, userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Set the task to another list
	 * 
	 * @param jwt    - the auth token from the user
	 * @param form   - form data for the operation
	 * @param taskID - task id
	 * @return - the ResponseEntity
	 */
	@PutMapping(value = "/api/task/list/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity moveTaskForList(@RequestHeader(AuthFilter.AUTH) String jwt,
	        @RequestBody MoveTaskToListForm form, @PathVariable("id") Integer taskID) {
		try {
			if (taskID == null || taskID <= 0 || form.getListId() == null || form.getListId() <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.moveTaskForList(taskID, form.getListId(), userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Move tasks from a List to another
	 * 
	 * @param jwt  - the auth token from the user
	 * @param form - form data for the operation
	 * @return - the ResponseEntity
	 */
	@PutMapping(value = "/api/taskList/tasks/moveFromList", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity moveTaskFromAListToAnother(@RequestHeader(AuthFilter.AUTH) String jwt,
	        @RequestBody MoveTasksFromListToListForm form) {
		try {
			Integer userID = JwtUtil.getIdUser(jwt);
			taskService.moveTaskFromAListToAnother(form.getListIdOrigin(), form.getListIdDestiny(), userID);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

}
