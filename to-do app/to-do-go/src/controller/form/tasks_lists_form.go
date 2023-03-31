package form

// TaskListForm form data to create or update list
type TaskListForm struct {
	ListName        string `json:"listName"`
	ListDescription string `json:"listDescription,omitempty"`
}

// MoveTasksFromListForm form data to move tasks from one list for other list
type MoveTasksFromListForm struct {
	ListIDOrigin  uint64 `json:"listIdOrigin"`
	ListIDDestiny uint64 `json:"listIdDestiny"`
}

// TaskForm form data for task creation and update
type TaskForm struct {
	TaskName        string `json:"taskName"`
	TaskDescription string `json:"taskDescription,omitempty"`
	Deadline        string `json:"deadline,omitempty"`
	ListID          uint64 `json:"listId,omitempty"`
}

// MoveTaskToListForm form data to move a task to another list
type MoveTaskToListForm struct {
	ListID uint64 `json:"listId,omitempty"`
}
