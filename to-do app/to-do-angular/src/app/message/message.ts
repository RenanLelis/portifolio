export function getMessage(codigo: string): string {
    if (messages.has(codigo)) {
        return messages.get(codigo)!;
    } else {
        return messages.get("MSE01")!;
    }
}

export function getErrorMessage(): string {
    return messages.get("MSE01")!;
}

export function getErrorMessageInputValues(): string {
    return messages.get("MSE02")!;
}

export function getErrorMessageInvalidToken(): string {
    return messages.get("MSE03")!;
}

export function getErrorMessageLogin(): string {
    return messages.get("MSE04")!;
}

export function getErrorMessageEmailExists(): string {
    return messages.get("MSE05")!;
}

export function getErrorMessageEmailNotFound(): string {
    return messages.get("MSE06")!;
}

export function getMessageOperationSucceded(): string {
    return messages.get("MS01")!;
}

export const messages: Map<string, string> = new Map<string, string>([
    ["MSE01", "An error ocurred on the operation."],
    ["MSE02", "Error on input values."],
    ["MSE03", "Invalid Authentication Token."],
    ["MSE04", "Invalid User or Password."],
    ["MSE05", "E-mail already exists."],
    ["MSE06", "E-mail not found."],
    ["MSE07", "User not activated."],
    ["MS01", "Operation Succeded."]
]);
