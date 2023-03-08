package com.renan.todo.controller.tasks;

import com.renan.todo.business.BusinessException;
import com.renan.todo.business.ListsTasksService;
import com.renan.todo.controller.filter.AutenticationFilter;
import com.renan.todo.controller.tasks.form.TaskForm;
import com.renan.todo.controller.tasks.form.TaskListForm;
import com.renan.todo.controller.tasks.form.TaskListMoveForm;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.util.JwtUtil;
import com.renan.todo.util.MessageUtil;
import com.renan.todo.util.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller for tasks and lists of tasks operations
 */
@RestController
public class TasksController {

    @Autowired
    private ListsTasksService taskService;

    /**
     * Get Tasks and TaskLists for the user
     *
     * @param jwt - the auth token of the user, to get user data
     * @return - Tasks and TaskLists
     */
    @CrossOrigin
    @GetMapping(path = "/api/taskList/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTasksAndLists(@RequestHeader(AutenticationFilter.AUTH) String jwt) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            return ResponseEntity.ok(taskService.getTasksAndLists(idUser));
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * Get Tasks in a TaskLists for the user
     *
     * @param jwt - the auth token of the user, to get user data
     * @return - tasks from that list
     */
    @CrossOrigin
    @GetMapping(path = "/api/taskList/{idList}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTasksByList(@RequestHeader(AutenticationFilter.AUTH) String jwt, @RequestParam("idList") Integer idList) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            return ResponseEntity.ok(taskService.getTasksByList(idList, idUser));
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * Get tasks without list
     *
     * @param jwt - the auth token of the user, to get user data
     * @return - tasks without list
     */
    @CrossOrigin
    @GetMapping(path = "/api/taskList/nolist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTasksUserWithoutList(@RequestHeader(AutenticationFilter.AUTH) String jwt) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            return ResponseEntity.ok(taskService.getTasksUserWithoutList(idUser));
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * Get lists from the user
     *
     * @param jwt - the auth token of the user, to get user data
     * @return - lists from the user
     */
    @CrossOrigin
    @GetMapping(path = "/api/taskList/lists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getLists(@RequestHeader(AutenticationFilter.AUTH) String jwt) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            return ResponseEntity.ok(taskService.getLists(idUser));
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * Create a new TaskList
     *
     * @param form - the input from the user
     * @param jwt  - the auth token of the user, to get user data
     * @return - the new taskList
     */
    @CrossOrigin
    @PostMapping(path = "/api/taskList/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTaskList(@RequestHeader(AutenticationFilter.AUTH) String jwt, @RequestBody TaskListForm form) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTaskList(form.getListName(), form.getListDescription(), idUser));
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * Update a TaskList
     *
     * @param form   - the input from the user
     * @param idList - list identifier
     * @param jwt    - the auth token of the user, to get user data
     * @return - Response Entity
     */
    @CrossOrigin
    @PutMapping(path = "/api/taskList/{idList}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTaskList(@RequestHeader(AutenticationFilter.AUTH) String jwt, @RequestBody TaskListForm form, @RequestParam("idList") Integer idList) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            taskService.updateTaskList(form.getListName(), form.getListDescription(), idList, idUser);
            return ResponseEntity.ok().build();
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * Delete a TaskList
     *
     * @param idList - list identifier
     * @param jwt    - the auth token of the user, to get user data
     * @return - the Response Entity
     */
    @CrossOrigin
    @DeleteMapping(path = "/api/taskList/{idList}")
    public ResponseEntity deleteTaskList(@RequestHeader(AutenticationFilter.AUTH) String jwt, @RequestParam("idList") Integer idList) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            taskService.deleteTaskList(idList, idUser);
            return ResponseEntity.ok().build();
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * Delete tasks from a list
     *
     * @param idList - list identifier
     * @param jwt    - the auth token of the user, to get user data
     * @return - the Response Entity
     */
    @CrossOrigin
    @DeleteMapping(path = "/api/taskList/tasks/{idList}")
    public ResponseEntity deleteTasksFromList(@RequestHeader(AutenticationFilter.AUTH) String jwt, @RequestParam("idList") Integer idList) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            taskService.deleteTasksFromList(idList, idUser);
            return ResponseEntity.ok().build();
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * move tasks from a List to another
     *
     * @param jwt  - the user auth token
     * @param form - input data
     * @return - Response Entity
     */
    @CrossOrigin
    @PostMapping(path = "/api/taskList/tasks/move")
    public ResponseEntity moveTasksForList(@RequestHeader(AutenticationFilter.AUTH) String jwt, @RequestBody TaskListMoveForm form) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            taskService.moveTasksForList(form.getIdOldList(), form.getIdNewList(), idUser, form.getTasks());
            return ResponseEntity.ok().build();
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * create a new task
     *
     * @param jwt  - the user auth token
     * @param form - form input for new task
     * @return the new task
     */
    @CrossOrigin
    @PostMapping(path = "/api/task/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTask(@RequestHeader(AutenticationFilter.AUTH) String jwt, @RequestBody TaskForm form) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(form.getTaskName(), form.getTaskDescription(), form.getDeadline(), form.getIdList(), idUser));
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * update the task
     *
     * @param id   - task id
     * @param jwt  - the user auth token
     * @param form - form input for new task
     * @return OK
     */
    @CrossOrigin
    @PutMapping(path = "/api/task/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTask(@RequestParam("id") Integer id, @RequestHeader(AutenticationFilter.AUTH) String jwt, @RequestBody TaskForm form) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            taskService.updateTask(id, form.getTaskName(), form.getTaskDescription(), form.getDeadline(), idUser);
            return ResponseEntity.ok().build();
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * delete the task
     *
     * @param id  - task id
     * @param jwt - the user auth token
     * @return OK
     */
    @CrossOrigin
    @DeleteMapping(path = "/api/task/{id}")
    public ResponseEntity deleteTask(@RequestParam("id") Integer id, @RequestHeader(AutenticationFilter.AUTH) String jwt) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            taskService.deleteTask(id, idUser);
            return ResponseEntity.ok().build();
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * delete the tasks
     *
     * @param idsTasks - tasks id
     * @param jwt      - the user auth token
     * @return OK
     */
    @CrossOrigin
    @DeleteMapping(path = "/api/task/")
    public ResponseEntity deleteTasks(@RequestParam("idsTasks") List<Integer> idsTasks, @RequestHeader(AutenticationFilter.AUTH) String jwt) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            taskService.deleteTasks(idsTasks, idUser);
            return ResponseEntity.ok().build();
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * Mark the task as complete
     *
     * @param id  - task id
     * @param jwt - the user auth token
     * @return OK
     */
    @CrossOrigin
    @PutMapping(path = "/api/task/complete/{id}")
    public ResponseEntity completeTask(@RequestParam("id") Integer id, @RequestHeader(AutenticationFilter.AUTH) String jwt) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            taskService.completeTask(id, idUser);
            return ResponseEntity.ok().build();
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }

    /**
     * Mark the task as incomplete
     *
     * @param id  - task id
     * @param jwt - the user auth token
     * @return OK
     */
    @CrossOrigin
    @PutMapping(path = "/api/task/uncomplete/{id}")
    public ResponseEntity uncompleteTask(@RequestParam("id") Integer id, @RequestHeader(AutenticationFilter.AUTH) String jwt) {
        try {
            Integer idUser = JwtUtil.getIdUser(jwt);
            taskService.unCompleteTask(id, idUser);
            return ResponseEntity.ok().build();
        } catch (UtilException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(MessageUtil.getErrorMessage()));
        } catch (BusinessException be) {
            return be.getResponseEntity();
        }
    }


}
