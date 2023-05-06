package com.renan.todo.service

import com.renan.todo.dto.TaskListDTO
import com.renan.todo.dto.TaskListDTOMapper
import com.renan.todo.model.TaskList
import com.renan.todo.model.User
import com.renan.todo.persistence.TaskListRepository
import com.renan.todo.persistence.TaskRepository
import com.renan.todo.util.getErrorMessage
import com.renan.todo.util.getErrorMessageInputValues
import java.util.stream.Collectors
import org.springframework.stereotype.Service

/**
 * Services for taskLists operation
 */
@Service
class TaskListServiceImpl(
    val taskListRepository: TaskListRepository,
    val taskRepository: TaskRepository,
    val taskService: TaskService,
    val taskListDTOMapper: TaskListDTOMapper,
    val utilService: UtilService
) : TaskListService {

    /**
     * Get the tasks and lists for the user
     */
    override fun getTasksAndLists(userID: Int?): List<TaskListDTO> {
        if (userID == null || userID <= 0)
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        try {
            val lists = taskListRepository.getListsByUser(userID)
            val tasks = taskRepository.getTasksByUser(userID)
            return taskListDTOMapper.convertMultipleListsAndTasks(lists!!, tasks)
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
        return java.util.ArrayList()
    }

    /**
     * Get the lists for the user
     */
    override fun getLists(userID: Int?): List<TaskListDTO> {
        if (userID == null || userID <= 0)
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        try {
            val lists = taskListRepository.getListsByUser(userID)
            return lists!!.stream().map(taskListDTOMapper).collect(Collectors.toList())
        } catch (e: Exception) {
            handleException(e)
        }
        return ArrayList()
    }

    /**
     * Create a new list for the user
     */
    override fun createList(listName: String?, listDescription: String?, userID: Int?): TaskListDTO {
        if (!validateNewListInput(listName!!, userID))
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        val user = User()
        user.id = userID
        val newList = TaskList()
        newList.listName = listName.trim { it <= ' ' }
        newList.user = user
        newList.listDescription = listDescription?.trim { it <= ' ' }
        return taskListDTOMapper.apply(taskListRepository.save(newList))
    }

    /**
     * Create a default list for a new user on the system
     */
    override fun createDefaultListForNewUser(userID: Int?) {
        createList(
            "My Tasks",
            "Default list for the user, created by the app",
            userID
        )
    }

    /**
     * Update name and description for the list
     */
    override fun updateTaskList(listName: String?, listDescription: String?, listID: Int?, userID: Int?) {
        if (!validateUpdateListInput(listName!!, listID, userID))
            throw BusinessException(getErrorMessageInputValues(), BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT)
        try {
            taskListRepository.updateTaskList(listName, listDescription, listID!!, userID!!)
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
    }

    /**
     * Delete the list and the tasks for the user
     */
    override fun deleteTaskList(listID: Int?, userID: Int?) {
        try {
            val lists = taskListRepository.getListsByUser(userID!!)
            if (lists.isNullOrEmpty()) {
                throw BusinessException(
                    getErrorMessageInputValues(),
                    BUSINESS_MESSAGE,
                    AppErrorType.INVALID_INPUT
                )
            }
            taskService.deleteTasksFromList(listID, userID)
            taskListRepository.deleteTaskList(listID!!, userID)
        } catch (e: java.lang.Exception) {
            handleException(e)
        }
    }

    /**
     * Validate input data for new list creation
     */
    private fun validateNewListInput(listName: String, userID: Int?) =
        listName.isNotEmpty() && userID != null && userID > 0

    /**
     * Validate input data for new list creation
     */
    private fun validateUpdateListInput(listName: String, listID: Int?, userID: Int?) =
        listName.isNotEmpty() && userID != null && userID > 0 && listID != null && listID > 0


    /**
     * handle exceptions for the operations
     */
    @Throws(BusinessException::class)
    private fun handleException(e: java.lang.Exception) {
        e.printStackTrace()
        if (e is BusinessException) throw e else throw BusinessException(
            getErrorMessage(),
            BUSINESS_MESSAGE,
            AppErrorType.INTERN_ERROR
        )
    }
}