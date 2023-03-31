package com.renan.todo.controller.form;

/**
 * Form for creation of task lists
 */
public class TaskListForm {

	private String listName;
	private String listDescription;

	public TaskListForm() {
		super();
	}

	public TaskListForm(String listName, String listDescription) {
		super();
		this.listName        = listName;
		this.listDescription = listDescription;
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

}
