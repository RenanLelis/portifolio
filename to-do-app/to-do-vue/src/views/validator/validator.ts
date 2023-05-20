export function isPasswordInputValid(password: string | null): boolean {
    if (password === null || password.trim() === '' || !/\d/.test(password) || password.trim().length < 6) {
        return false;
    }
    return true;
}

export function isEmailValid(email: string | null): boolean {
    if (email === null || email.trim().length === 0) return false;
    const expression: RegExp = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
    return expression.test(email.trim());
}