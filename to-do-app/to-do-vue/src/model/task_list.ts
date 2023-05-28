import type { Task } from "./task";

export interface TaskList {
    id : number;
    listName : string;
    listDescription : string;
    tasks : Task[];
}