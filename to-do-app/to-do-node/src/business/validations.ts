import validator from 'email-validator';
import { MIN_LENGTH_PASSWORD } from './consts';

export const isPasswordParametersValid = (password: string | null): boolean => {
    return !(password === null || password === '' || !/\d/.test(password) || password.length < MIN_LENGTH_PASSWORD)
}

export const isEmailValid = (email: string | null): boolean => {
    return email !== null && validator.validate(email);
}