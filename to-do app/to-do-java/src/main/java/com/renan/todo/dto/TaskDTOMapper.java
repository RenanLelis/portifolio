package com.renan.todo.dto;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.renan.todo.model.Task;

@Service
public class TaskDTOMapper implements Function<Task, TaskDTO> {

	/**
	 * Convert a task to an taskDTO
	 *
	 * @param task - object to convert to DTO
	 * @return - the dto
	 */
	@Override
	public TaskDTO apply(Task task) {
		return new TaskDTO(task.getId(), task.getTaskName(), task.getTaskDescription(), task.getDeadline(),
		        task.getTaskStatus());
	}
}
