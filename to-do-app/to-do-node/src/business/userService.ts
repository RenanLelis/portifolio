import msg from './messages/messages'
import { Error, AppErrorType } from "./errorType";
import { isPasswordParametersValid } from './validations';
import userRepo from '../persistence/userRepository'
import authService from './authService'

const updateUserProfile = (userID: number | null, firstName: string | null, lastName: string | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (firstName === null || firstName.trim().length <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        userRepo.UpdateUser(userID, firstName.trim(), lastName !== null ? lastName.trim() : null)
            .then(() => { return resolve(null) })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    })
}

const updateUserPassword = (userID: number | null, password: string | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (!isPasswordParametersValid(password))
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        const hashPassword = authService.generateHashString(password!.trim())
        userRepo.updatePasswordById(userID, hashPassword)
            .then(() => { return resolve(null) })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    })
}

export default {
    updateUserProfile,
    updateUserPassword
}