package com.renan.todo.controller.form

/**
 * Form for login operation
 */
class LoginForm {
    val email: String = ""
    val password: String = ""
}

/**
 * Form for password recover operations
 */
class RecoverPasswordForm {
    val email: String = ""
}

/**
 * Form for password reset operations
 */
class PasswordResetForm {
    val email: String = ""
    val password: String = ""
    val newPasswordCode: String = ""
}

/**
 * Form for new user registration
 */
class UserRegistrationForm {
    val email: String = ""
    val password: String = ""
    val firstName: String = ""
    val lastName: String? = null
}

/**
 * Form for new user activation
 */
class UserActivationForm {
    val email: String = ""
    val activationCode: String = ""
}

/**
 * Form for request a user activation
 */
class RequestUserActivationForm {
    val email: String = ""
}

/**
 * Form for user profile update
 */
class UserProfileForm {
    val firstName: String = ""
    val lastName: String? = null
}

/**
 * Form for user password update
 */
class UserPasswordForm {
    val password: String = ""
}

/**
 * Form for creation of task lists
 */
class TaskListForm {
    val listName: String = ""
    val listDescription: String? = null
}

/**
 * Form for task operations
 */
class TaskForm {
    val taskName: String = ""
    val taskDescription: String? = null
    val deadline: String? = null
    val listId: Int = 0
}

/**
 * Form for update of task
 */
class UpdateTaskForm {
    val taskName: String = ""
    val taskDescription: String? = null
    val deadline: String? = null
}

/**
 * Form to update a list set to a task
 */
class MoveTaskToListForm {
    val listId: Int = 0
}

/**
 * form to update tasks from a list, setting them to another
 */
class MoveTasksFromListToListForm {
    val listIdOrigin: Int = 0
    val listIdDestiny: Int = 0
}