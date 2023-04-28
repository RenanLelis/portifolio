import { Task } from "./task";

export interface TaskList {
    id : number | null;
    listName : string;
    listDescription : string | null;
    tasks : Task[];
}