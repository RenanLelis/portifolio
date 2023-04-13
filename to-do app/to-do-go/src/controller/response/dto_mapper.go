package response

import (
	"errors"

	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/model"
	"renan.com/todo/src/security"
)

// ConvertUserToUserDTO create a DTO with the user as param
func ConvertUserToUserDTO(user model.User) (UserDTO, error) {
	jwt, err := security.CreateToken(user.ID, user.Status, user.Email, user.FirstName, user.LastName)
	if err != nil {
		return UserDTO{}, errors.New(messages.GetErrorMessageToken())
	}
	return UserDTO{
		ID:        user.ID,
		Email:     user.Email,
		Status:    user.Status,
		Name:      user.FirstName,
		LastName:  user.LastName,
		ExpiresIn: uint64(security.EXP_TIME),
		JWT:       jwt,
	}, nil
}

// ConvertTaskListToDTO convert a model TaskList to DTO
func ConvertTaskListToDTO(taskList model.TaskList) TaskListDTO {
	return TaskListDTO{
		ID:              taskList.ID,
		ListName:        taskList.ListName,
		ListDescription: taskList.ListDescription,
		Tasks:           make([]TaskDTO, 0),
	}
}

// ConvertTasksToDTO convert tasks to DTO
func ConvertTasksToDTO(tasks []model.Task) []TaskDTO {
	if tasks == nil || len(tasks) <= 0 {
		return make([]TaskDTO, 0)
	}
	var result []TaskDTO = make([]TaskDTO, 0)
	for _, task := range tasks {
		result = append(result, ConvertTaskToDTO(task))
	}
	return result
}

// ConvertTaskToDTO convert a task to DTO
func ConvertTaskToDTO(task model.Task) TaskDTO {
	return TaskDTO{
		ID:              task.ID,
		TaskName:        task.TaskName,
		TaskDescription: task.TaskDescription,
		Deadline:        task.Deadline,
		TaskStatus:      task.TaskStatus,
	}
}

// ConvertTasksAndListsToDTO convert the model for tasks and lists to DTO
func ConvertTasksAndListsToDTO(lists []model.TaskList, tasks []model.Task) []TaskListDTO {
	var listDTO []TaskListDTO = make([]TaskListDTO, 0)
	listDTO = append(listDTO, TaskListDTO{
		ID:              0,
		ListName:        "Default",
		ListDescription: "Default List for tasks",
		Tasks:           make([]TaskDTO, 0),
	})
	for _, tList := range lists {
		listDTO = append(listDTO, ConvertTaskListToDTO(tList))
	}

	for _, task := range tasks {
		for i := 0; i < len(listDTO); i++ {
			if listDTO[i].ID == task.ID {
				listDTO[i].Tasks = append(listDTO[i].Tasks, ConvertTaskToDTO(task))
				break
			}
		}
	}
	return listDTO
}
