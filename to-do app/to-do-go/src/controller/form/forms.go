package form

// LoginForm form data for login operations
type LoginForm struct {
	Email    string `json:"email"`
	Password string `json:"password"`
}

// RecoverPasswordForm form data to recover password
type RecoverPasswordForm struct {
	Email string `json:"email"`
}

// ResetPasswordForm form data to reset password with code
type ResetPasswordForm struct {
	Email           string `json:"email"`
	Password        string `json:"password"`
	NewPasswordCode string `json:"newPasswordCode"`
}

// NewUserForm form data for new user registration
type NewUserForm struct {
	Email     string `json:"email"`
	Password  string `json:"password"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName,omitempty"`
}

// UserActivationForm form data for user activation
type UserActivationForm struct {
	Email          string `json:"email"`
	ActivationCode string `json:"activationCode"`
}

// UpdatePasswordForm form data for user password update when user is logged in
type UpdatePasswordForm struct {
	Password string `json:"password"`
}

// UserProfileFom form data for user profile update
type UserProfileFom struct {
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName,omitempty"`
}

// TaskListForm form data to create or update list
type TaskListForm struct {
	ListName        string `json:"listName"`
	ListDescription string `json:"listDescription,omitempty"`
}

// DeleteTaskListForm form data to delete list
type DeleteTaskListForm struct {
	ListID uint64 `json:"id,omitempty"`
}

// MoveTasksToListForm form data to move tasks for other list
type MoveTasksToListForm struct {
	ListID   uint64   `json:"listId"`
	TasksIDs []uint64 `json:"tasksIds"`
}

// MoveTasksFromListForm form data to move tasks from one list for other list
type MoveTasksFromListForm struct {
	ListIDOrigin  uint64 `json:"listIdOrigin"`
	ListIDDestiny uint64 `json:"listIdDestiny"`
}

// CompleteTaskListForm form data for complete or uncomplete tasks from a list
// type CompleteTaskListForm struct {
// 	ListID uint64 `json:"listId"`
// }

// TaskForm form data for task creation and update
type TaskForm struct {
	TaskName        string `json:"taskName"`
	TaskDescription string `json:"taskDescription,omitempty"`
	Deadline        string `json:"deadline,omitempty"`
	ListID          uint64 `json:"listId,omitempty"`
}

// MoveTaskToListForm form data to move a task to another list
type MoveTaskToListForm struct {
	ID     uint64 `json:"id"`
	ListID uint64 `json:"listId,omitempty"`
}

// DeleteOrCompleteTaskForm form data to delete a task or update status to complete or incomplete
// type DeleteOrCompleteTaskForm struct {
// 	ID uint64 `json:"id"`
// }

// DeleteOrCompleteTasksForm form data to delete tasks or update status of tasks to complete or incomplete
type DeleteOrCompleteTasksForm struct {
	IDs []uint64 `json:"idsTasks"`
}
