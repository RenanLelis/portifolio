package com.renan.todo.controller.form;

/**
 * Form to update a list set to a task
 */
public class MoveTaskToListForm {

	private Integer listId;

	public MoveTaskToListForm() {
		super();
	}

	public MoveTaskToListForm(Integer listId) {
		super();
		this.listId = listId;
	}

	public Integer getListId() {
		return listId;
	}

	public void setListId(Integer listId) {
		this.listId = listId;
	}

}
