package com.renan.todo.dto;

/**
 * Transfer Object for Task
 */
public class TaskDTO {

	private Integer id;
	private String  taskName;
	private String  taskDescription;
	private String  deadline;
	private Integer taskStatus;

	public TaskDTO() {
		super();
	}

	public TaskDTO(Integer id, String taskName, String taskDescription, String deadline, Integer taskStatus) {
		super();
		this.id              = id;
		this.taskName        = taskName;
		this.taskDescription = taskDescription;
		this.deadline        = deadline;
		this.taskStatus      = taskStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

}
