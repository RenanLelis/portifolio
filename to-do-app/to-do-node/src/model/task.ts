import { STATUS_INCOMPLETE } from "../business/consts";

export class Task {
    constructor(
        public id: number | null = null,
        public taskName: string = "",
        public taskDescription: string | null = null,
        public deadline: string | null = null,
        public taskStatus: number = STATUS_INCOMPLETE,
        public userID: number = 0,
        public listID: number = 0,
    ) { }
}
