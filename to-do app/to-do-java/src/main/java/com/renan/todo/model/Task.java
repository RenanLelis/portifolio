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
 * Task Entity class
 */
@Entity
@Table(name = "TASK")
public class Task {

	public static final Integer STATUS_INCOMPLETE = 0;
	public static final Integer STATUS_COMPLETE   = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "TASK_NAME", nullable = false)
	private String taskName;

	@Column(name = "TASK_DESCRIPTION")
	private String taskDescription;

	@Column(name = "DEADLINE")
	private String deadline;

	@Column(name = "TASK_STATUS", nullable = false)
	private Integer taskStatus;

	@ManyToOne
	@JoinColumn(name = "ID_USER", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "ID_LIST")
	private TaskList list;

	public Task() {
		super();
	}

	public Task(Integer id, String taskName, String taskDescription, String deadline, Integer taskStatus, User user,
	        TaskList list) {
		super();
		this.id              = id;
		this.taskName        = taskName;
		this.taskDescription = taskDescription;
		this.deadline        = deadline;
		this.taskStatus      = taskStatus;
		this.user            = user;
		this.list            = list;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TaskList getList() {
		return list;
	}

	public void setList(TaskList list) {
		this.list = list;
	}
}
