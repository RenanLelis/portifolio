import { Task } from "./task";

export interface TaskList {
    id : number | null;
    listName : string;
    listDescription : string | null;
    tasks : Task[] | null;
}

export function createDefaultTaskList(): TaskList {
    const defaultTaskList: TaskList = {
        id: null,
        listName: "Tasks - Default List",
        listDescription: "Default List for the user, cannot be changed",
        tasks: []
    };
    return defaultTaskList;
}