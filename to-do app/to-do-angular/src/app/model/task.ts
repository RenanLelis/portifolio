export interface Task {

    id : Number;
    taskName : string;
    taskDescription : string | null;
    deadline : string | null;
    taskStatus : Number;
}

export const STATUS_INCOMPLETE = 0;
export const STATUS_COMPLETE = 1;