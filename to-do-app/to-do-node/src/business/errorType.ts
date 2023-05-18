export enum AppErrorType {
    INTERN_ERROR = 500,
    NOT_ALLOWED = 401,
    INVALID_INPUT = 400,
}

export class Error {
    constructor(public errorType: AppErrorType, public errorMessage: string) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }
}

