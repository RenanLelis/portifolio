package response

type TaskDTO struct {
	ID              uint64 `json:"id"`
	TaskName        string `json:"taskName"`
	TaskDescription string `json:"taskDescription"`
	Deadline        string `json:"deadline"`
	TaskStatus      uint64 `json:"taskStatus"`
}

type TaskListDTO struct {
	ID              uint64    `json:"id"`
	ListName        string    `json:"listName"`
	ListDescription string    `json:"listDescription"`
	Tasks           []TaskDTO `json:"tasks"`
}
