package model

const STATUS_TASK_COMPLETE uint64 = 1
const STATUS_TASK_INCOMPLETE uint64 = 0

// TaskList represents the TASK_LIST Table on the database
type TaskList struct {
	ID              uint64
	ListName        string
	ListDescription string
	UserID          uint64
}

// Task represents the TASK Table on the database
type Task struct {
	ID              uint64
	TaskName        string
	TaskDescription string
	Deadline        string
	TaskStatus      uint64
	UserID          uint64
	ListID          uint64
}
