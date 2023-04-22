export interface Task {
    id : number;
    taskName : string;
    taskDescription : string | null;
    deadline : string | null;
    taskStatus : number;
}

export const STATUS_INCOMPLETE = 0;
export const STATUS_COMPLETE = 1;