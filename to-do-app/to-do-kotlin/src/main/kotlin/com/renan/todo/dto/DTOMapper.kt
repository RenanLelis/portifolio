package com.renan.todo.dto

import com.renan.todo.model.Task
import com.renan.todo.model.TaskList
import com.renan.todo.model.User
import com.renan.todo.service.JwtService
import com.renan.todo.util.STATUS_INACTIVE
import com.renan.todo.util.STATUS_INCOMPLETE
import com.renan.todo.util.TIMEOUT
import java.util.function.Function
import java.util.stream.Collectors
import org.springframework.stereotype.Service

/**
 * Mapper to create a UserDTO from a User Object
 */
@Service
class UserDTOMapper(val jwtService: JwtService) : Function<User, UserDTO> {

    /**
     * Convert a user to an userDTO, generating the JWT
     */
    override fun apply(user: User): UserDTO {
        val userDTO = UserDTO()
        userDTO.email = user.email ?: ""
        userDTO.jwt = jwtService.generateJWT(user.id, user.userStatus, user.email, user.firstName, user.lastName) ?: ""
        userDTO.id = user.id ?: 0
        userDTO.status = user.userStatus ?: STATUS_INACTIVE
        userDTO.expiresIn = TIMEOUT
        userDTO.name = user.firstName ?: ""
        userDTO.lastName = user.lastName ?: ""
        return userDTO;
    }
}

/**
 * Mapper from Task to DTO
 */
@Service
class TaskDTOMapper: Function<Task, TaskDTO> {

    /**
     * Convert a task to an taskDTO
     */
    override fun apply(task: Task): TaskDTO {
        val taskDTO = TaskDTO()
        taskDTO.id = task.id ?: 0
        taskDTO.taskName = task.taskName ?: ""
        taskDTO.taskDescription = task.taskDescription ?: ""
        taskDTO.deadline = task.deadline ?: ""
        taskDTO.taskStatus = task.taskStatus ?: STATUS_INCOMPLETE
        return taskDTO
    }
}

/**
 * Mapper from TaskList to TaskListDTO
 */
@Service
class TaskListDTOMapper(private val taskDTOMapper: TaskDTOMapper) : Function<TaskList, TaskListDTO> {

    /**
     * Convert a taskList to an taskListDTO
     */
    override fun apply(taskList: TaskList): TaskListDTO {
        val taskListDTO = TaskListDTO()
        taskListDTO.id = taskList.id ?: 0
        taskListDTO.listName = taskList.listName ?: ""
        taskListDTO.listDescription = taskList.listDescription ?: ""
        taskListDTO.tasks = ArrayList()
        return taskListDTO
    }

    /**
     * Convert a list of tasks (from the same taskList) to a TaskListDTO
     */
    fun convertToListDTO(tasks: List<Task>?): TaskListDTO? {
        if (!tasks.isNullOrEmpty()) {
            val result = this.apply(tasks[0].list!!)
            result.tasks = tasks.stream().map<TaskDTO>(taskDTOMapper).collect(Collectors.toList<TaskDTO>())
            return result
        }
        return null
    }

    /**
     * Convert multiple lists of tasks and tasks to DTO
     */
    fun convertMultipleListsAndTasks(tasks: List<Task>?): List<TaskListDTO> {
        var result: List<TaskListDTO> = ArrayList()
        if (!tasks.isNullOrEmpty()) {
            val listsTasks: MutableList<TaskList> = java.util.ArrayList()
            for (task in tasks) {
                if (!listsTasks.contains(task.list) && task.list != null) {
                    listsTasks.add(task.list!!)
                }
            }
            result = this.convertMultipleListsAndTasks(listsTasks, tasks)
        }
        return result
    }

    /**
     * Convert multiple lists of tasks and tasks to DTO
     */
    fun convertMultipleListsAndTasks(taskLists: List<TaskList>, tasks: List<Task>?): List<TaskListDTO> {
        val result = taskLists.stream().map<TaskListDTO>(this).collect(Collectors.toList())
        if (!tasks.isNullOrEmpty()) {
            for (taskListDTO in result) {
                taskListDTO.tasks = tasks.stream().filter { task: Task -> task.list!!.id == taskListDTO.id }
                    .map<TaskDTO>(taskDTOMapper).collect(Collectors.toList<TaskDTO>())
            }
        }
        return result
    }

    /**
     * Convert a list of tasks and taskList to DTO
     */
    fun convert(taskList: TaskList?, tasks: List<Task>): TaskListDTO {
        val taskListDTO = this.apply(taskList!!)
        taskListDTO.tasks = tasks.stream().map<TaskDTO>(taskDTOMapper).collect(Collectors.toList<TaskDTO>())
        return taskListDTO
    }

}