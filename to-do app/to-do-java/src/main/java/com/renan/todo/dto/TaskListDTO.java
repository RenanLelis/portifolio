package com.renan.todo.dto;

import java.util.List;

/**
 * Transfer Object for TaskList
 */
public class TaskListDTO {

	private Integer       id;
	private String        listName;
	private String        listDescription;
	private List<TaskDTO> tasks;

	public TaskListDTO() {
		super();
	}

	public TaskListDTO(Integer id, String listName, String listDescription, List<TaskDTO> tasks) {
		super();
		this.id              = id;
		this.listName        = listName;
		this.listDescription = listDescription;
		this.tasks           = tasks;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getListDescription() {
		return listDescription;
	}

	public void setListDescription(String listDescription) {
		this.listDescription = listDescription;
	}

	public List<TaskDTO> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskDTO> tasks) {
		this.tasks = tasks;
	}

}
