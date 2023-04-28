import { Task } from "./task";

export interface TaskList {
    id : number;
    listName : string;
    listDescription : string | null;
    tasks : Task[];
}