import type { Task } from "./task";

export interface TaskList {
    id : number | null;
    listName : string;
    listDescription : string | null;
    tasks : Task[];
}

export function createDefaultTaskList(): TaskList {
    const defaultTaskList: TaskList = {
        id: null,
        listName: "My Tasks",
        listDescription: "Default List for the user, cannot be changed",
        tasks: []
    };
    return defaultTaskList;
}