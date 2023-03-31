package com.renan.todo.controller.form;

/**
 * form to update tasks from a list, setting them to another
 */
public class MoveTasksFromListToListForm {

	private Integer listIdOrigin;
	private Integer listIdDestiny;

	public MoveTasksFromListToListForm() {
		super();
	}

	public MoveTasksFromListToListForm(Integer listIdOrigin, Integer listIdDestiny) {
		super();
		this.listIdOrigin  = listIdOrigin;
		this.listIdDestiny = listIdDestiny;
	}

	public Integer getListIdOrigin() {
		return listIdOrigin;
	}

	public void setListIdOrigin(Integer listIdOrigin) {
		this.listIdOrigin = listIdOrigin;
	}

	public Integer getListIdDestiny() {
		return listIdDestiny;
	}

	public void setListIdDestiny(Integer listIdDestiny) {
		this.listIdDestiny = listIdDestiny;
	}

}
