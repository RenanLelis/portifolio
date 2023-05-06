package com.renan.todo.dto

import com.renan.todo.util.STATUS_INACTIVE
import com.renan.todo.util.STATUS_INCOMPLETE

/**
 * Transfer Object for Error Message
 */
class ErrorDTO(val errorMessage: String = "")

/**
 * User data to return as login operation
 */
class UserDTO {
    var email: String = ""
    var jwt: String = ""
    var id: Int = 0
    var status: Int = STATUS_INACTIVE
    var expiresIn: Int = 0
    var name: String = ""
    var lastName: String? = null
}

/**
 * Transfer Object for Task
 */
class TaskDTO {
    var id: Int = 0
    var taskName: String = ""
    var taskDescription: String? = null
    var deadline: String? = null
    var taskStatus: Int = STATUS_INCOMPLETE
}

/**
 * Transfer Object for TaskList
 */
class TaskListDTO {
    var id: Int = 0
    var listName: String = ""
    var listDescription: String? = null
    var tasks: List<TaskDTO> = ArrayList()
}