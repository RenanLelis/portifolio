import { Task } from "./task";

export interface TaskList {
    id : Number;
    listName : string;
    listDescription : string | null;
    tasks : Task[] | null;
}