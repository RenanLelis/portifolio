module.exports = class Task {

    static STATUS_INCOMPLETE = 0;
    static STATUS_COMPLETE = 1;

    constructor(id, taskName, taskDescription, deadline, taskStatus, idList, idUser) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.deadline = deadline;
        this.taskStatus = taskStatus;
        this.idList = idList;
        this.idUser = idUser;
    }
}
