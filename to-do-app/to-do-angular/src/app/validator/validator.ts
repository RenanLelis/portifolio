import { FormControl, ValidationErrors } from '@angular/forms';

export function isPasswordInputValid(password: string) {
    if (password === null || password === '' || !/\d/.test(password) || password.length < 6) {
        return false;
    }
    return true;
}

export function passwordValidation(control: FormControl): ValidationErrors | null {
    if (!isPasswordInputValid(control.value)) {
        return { "invalidPassword": true };
    }
    return null;
}