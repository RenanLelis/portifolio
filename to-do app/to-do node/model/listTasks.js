module.exports = class ListTasks {
    constructor(id, listName, listDescription, idUser, tasks) {
        this.id = id;
        this.listName = listName;
        this.listDescription = listDescription;
        this.idUser = idUser;
        this.tasks = tasks;
    }
}