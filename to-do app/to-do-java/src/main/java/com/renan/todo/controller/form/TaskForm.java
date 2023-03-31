package com.renan.todo.controller.form;

/**
 * Form for task operations
 */
public class TaskForm {

	private String  taskName;
	private String  taskDescription;
	private String  deadline;
	private Integer idList;

	public TaskForm() {
		super();
	}

	public TaskForm(String taskName, String taskDescription, String deadline, Integer idList) {
		super();
		this.taskName        = taskName;
		this.taskDescription = taskDescription;
		this.deadline        = deadline;
		this.idList          = idList;
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

	public Integer getIdList() {
		return idList;
	}

	public void setIdList(Integer idList) {
		this.idList = idList;
	}

}
