import { STATUS_INACTIVE } from "../business/consts";

export class User {
    constructor(
        public id: number | null = null,
        public firstName: string = "",
        public lastName: string | null = null,
        public email: string = "",
        public password: string | null = null,
        public activationCode: string | null = null,
        public newPasswordCode: string | null = null,
        public userStatus: number = STATUS_INACTIVE,
    ) { }
}
 