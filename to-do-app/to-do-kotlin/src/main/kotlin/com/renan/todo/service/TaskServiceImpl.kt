package com.renan.todo.service

import com.renan.todo.dto.TaskDTO
import com.renan.todo.dto.TaskDTOMapper
import com.renan.todo.model.Task
import com.renan.todo.model.TaskList
import com.renan.todo.model.User
import com.renan.todo.persistence.TaskListRepository
import com.renan.todo.persistence.TaskRepository
import com.renan.todo.util.STATUS_COMPLETE
import com.renan.todo.util.STATUS_INCOMPLETE
import com.renan.todo.util.getErrorMessage
import com.renan.todo.util.getErrorMessageInputValues
import java.util.stream.Collectors
import org.springframework.stereotype.Service

/**
 * Services for tasks operation
 */
@Service
class TaskServiceImpl(
    val taskRepository: TaskRepository,
    val taskListRepository: TaskListRepository,
    val taskDTOMapper: TaskDTOMapper,
    val utilService: UtilService
) : TaskService {

    /**
     * Get the tasks for the list passed
     */
    override fun getTasksByList(listID: Int?, userID: Int?): List<TaskDTO?>? {
        if (listID == null || listID <= 0 || userID == null || userID <= 0)
            throw BusinessException(
                getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT
            )
        try {
            val tasks = taskRepository.getTasksByList(listID, userID)
            return if (tasks.isNullOrEmpty()) {
                ArrayList()
            } else tasks.stream().map(taskDTOMapper).collect(Collectors.toList())
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
        return ArrayList()
    }

    /**
     * Delete all tasks from the list
     */
    override fun deleteTasksFromList(listID: Int?, userID: Int?) {
        if (listID == null || listID <= 0 || userID == null || userID <= 0)
            throw BusinessException(
                getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT
            )
        try {
            taskRepository.deleteTasksFromList(listID, userID)
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
    }

    /**
     * Mark as complete all tasks from the list
     */
    override fun completeTasksFromList(listID: Int?, userID: Int?) =
        updateStatusTasksFromList(listID, userID, STATUS_COMPLETE)

    override fun incompleteTasksFromList(listID: Int?, userID: Int?) =
        updateStatusTasksFromList(listID, userID, STATUS_INCOMPLETE)

    /**
     * Update the status for all tasks from a list
     */
    @Throws(BusinessException::class)
    private fun updateStatusTasksFromList(listID: Int?, userID: Int?, status: Int?) {
        if (listID == null || listID <= 0 || status == null || status != STATUS_COMPLETE && status != STATUS_INCOMPLETE || userID == null || userID <= 0) throw BusinessException(
            getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT
        )
        try {
            taskRepository.updateStatusTasksFromList(listID, userID, status)
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
    }

    /**
     * Change the listID on all tasks from a list
     */
    override fun moveTasksFromList(listIDOrigin: Int?, listIDDestiny: Int?, userID: Int?) {
        if (listIDOrigin == null || listIDOrigin <= 0 || listIDDestiny == null || listIDDestiny <= 0 || userID == null || userID <= 0)
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        try {
            taskRepository.moveTasksFromList(listIDOrigin, listIDDestiny, userID)
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
    }

    /**
     * Create a new task for the user
     */
    override fun createTask(
        taskName: String?,
        taskDescription: String?,
        deadline: String?,
        listID: Int?,
        userID: Int?
    ): TaskDTO? {
        if (!validateNewTaskInput(taskName, deadline, listID, userID))
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        try {
            val user = User()
            user.id = userID
            val list = TaskList()
            list.id = listID
            val newTask = Task()
            newTask.id = null
            newTask.taskName = taskName!!.trim { it <= ' ' }
            newTask.taskDescription = taskDescription?.trim { it <= ' ' }
            newTask.deadline = deadline?.trim { it <= ' ' }
            newTask.taskStatus = STATUS_INCOMPLETE
            newTask.user = user
            newTask.list = list
            return taskDTOMapper.apply(taskRepository.save(newTask))
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
        return null
    }

    /**
     * Validate the input data for new task
     */
    private fun validateNewTaskInput(
        taskName: String?, deadline: String?, listID: Int?, userID: Int?
    ): Boolean {
        return (listID != null && listID > 0 && userID != null && userID > 0 && !taskName.isNullOrEmpty()
                && isValidDeadline(deadline))
    }

    /**
     * Update a task on database
     */
    override fun updateTask(
        taskName: String?,
        taskDescription: String?,
        deadline: String?,
        taskID: Int?,
        userID: Int?
    ) {
        if (!validateUpdateTaskInput(taskName!!, deadline!!, taskID, userID))
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        try {
            taskRepository.updateTask(
                taskName.trim { it <= ' ' },
                taskDescription?.trim { it <= ' ' },
                deadline.trim { it <= ' ' },
                taskID!!, userID!!
            )
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
    }

    /**
     * change the list for the task
     */
    override fun moveTaskToList(taskID: Int?, listIDDestiny: Int?, userID: Int?) {
        if ((taskID == null) || (taskID <= 0) || (userID == null) || (userID <= 0) || (listIDDestiny == null) || (listIDDestiny <= 0))
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        try {
            taskListRepository.getListByIdAndUser(listIDDestiny, userID) ?: throw BusinessException(
                getErrorMessageInputValues(),
                BUSINESS_MESSAGE,
                AppErrorType.INVALID_INPUT
            )
            taskRepository.moveTaskToList(taskID, listIDDestiny, userID)
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
    }

    /**
     * Delete the task on the database
     */
    override fun deleteTask(taskID: Int?, userID: Int?) {
        if (taskID == null || taskID <= 0 || userID == null || userID <= 0)
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        try {
            taskRepository.deleteTask(taskID, userID)
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
    }

    /**
     * Mark the task as complete
     */
    override fun completeTask(taskID: Int?, userID: Int?) =
        updateStatusTask(taskID, userID, STATUS_COMPLETE)

    /**
     * Mark the task as incomplete
     */
    override fun incompleteTask(taskID: Int?, userID: Int?) =
        updateStatusTask(taskID, userID, STATUS_INCOMPLETE)

    /**
     * Update the status for a task
     */
    @Throws(BusinessException::class)
    private fun updateStatusTask(taskID: Int?, userID: Int?, status: Int) {
        if (taskID == null || taskID <= 0 || userID == null || userID <= 0)
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        try {
            taskRepository.updateTaskStatus(taskID, userID, status)
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
    }

    /**
     * Validate string deadline
     */
    private fun isValidDeadline(deadline: String?) =
        if (deadline.isNullOrEmpty()) true else utilService.isValidDate(deadline)


    /**
     * Validate the input data for new task
     */
    private fun validateUpdateTaskInput(
        taskName: String, deadline: String, taskID: Int?, userID: Int?
    ) = (taskID != null && taskID > 0 && userID != null && userID > 0 && !taskName.isNullOrEmpty()
            && isValidDeadline(deadline))

    /**
     * handle exceptions for the operations
     */
    @Throws(BusinessException::class)
    private fun handleException(e: Exception) {
        e.printStackTrace()
        if (e is BusinessException) throw e else throw BusinessException(
            getErrorMessage(), BUSINESS_MESSAGE, AppErrorType.INTERN_ERROR
        )
    }
}