export class TaskList {
    constructor(
        public id: number | null = null,
        public listName: string = "",
        public listDescription: string | null = null,
        public userID: number = 0,
    ) { }
}
