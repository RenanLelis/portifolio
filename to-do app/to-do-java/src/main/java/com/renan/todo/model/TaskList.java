package com.renan.todo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * List Entity class
 */
@Entity
@Table(name = "TASK_LIST")
public class TaskList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "LIST_NAME", nullable = false)
	private String listName;

	@Column(name = "LIST_DESCRIPTION", nullable = true)
	private String listDescription;

	@ManyToOne
	@JoinColumn(name = "ID_USER", nullable = false)
	private User user;

	public TaskList() {
		super();
	}

	public TaskList(Integer id, String listName, String listDescription, User user) {
		super();
		this.id              = id;
		this.listName        = listName;
		this.listDescription = listDescription;
		this.user            = user;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
