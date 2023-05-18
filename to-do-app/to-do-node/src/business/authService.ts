import msg from './messages/messages'
import { Error, AppErrorType } from './errorType';
import { LENGTH_ACTIVATION_CODE, LENGTH_NEW_PASSWORD_CODE, STATUS_ACTIVE, STATUS_INACTIVE } from './consts';
import { isEmailValid, isPasswordParametersValid } from './validations';
import userRepo from '../persistence/userRepository'
import bcrypt from 'bcrypt';
import { User } from '../model/user';
import { UserDTO, convertUserToDTO } from './dto/dto';
import mailService from './mailService';
import taskListService from './taskListService';

const saltRounds = 10;

const login = (email: string | null, password: string | null): Promise<UserDTO> => {
    return new Promise((resolve, reject) => {
        if (!validateLoginData(email, password))
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        userRepo.getUserByEmail(email!.trim().toLowerCase())
            .then(user => {
                if (user === null) return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageLogin()));
                const usr = (user as User)
                bcrypt.compare(password!.trim(), usr.password!, function (err, res) {
                    if (err && err !== null)
                        return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
                    if (!res)
                        return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageLogin()));
                    if (usr.userStatus <= STATUS_INACTIVE)
                        return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageUserNotActive()));
                    return resolve(convertUserToDTO(usr));
                });
            })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    })
}

const recoverPassword = (email: string | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (!isEmailValid(email))
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        userRepo.getUserByEmail(email!.trim().toLowerCase())
            .then(user => {
                if (user === null) return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
                const usr = (user as User)
                const newPasswordCode = generateNewPasswordCode();
                userRepo.updateUserNewPasswordCode(usr.email, newPasswordCode)
                    .then(() => {
                        try {
                            mailService.sendEmailRecoverPassword(usr.email, newPasswordCode);
                            return resolve(null);
                        } catch (error) {
                            console.log(error);
                            return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage()));
                        }
                    })
                    .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
            })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
    })
}

const resetPassword = (email: string | null, newPasswordCode: string | null, newPassword: string | null): Promise<UserDTO> => {
    return new Promise((resolve, reject) => {
        if (!validateResetPasswordInput(email, newPasswordCode, newPassword))
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        userRepo.getUserByEmail(email!.trim().toLowerCase())
            .then(user => {
                if (user === null) return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
                const usr = (user as User);
                const hashPassword = generateHashString(newPassword!.trim());
                userRepo.updatePasswordByCode(email!, newPasswordCode!, hashPassword)
                    .then(() => { return resolve(convertUserToDTO(usr)); })
                    .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })

            })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
    })
}

const registerNewUser = (email: string | null, password: string | null, firstName: string | null, lastName: string | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (!validateNewUserData(email, password, firstName, lastName))
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        userRepo.getUserByEmail(email!.trim().toLowerCase())
            .then(user => {
                if (user !== null && (user as User).id !== null && (user as User).id! > 0)
                    return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageEmailAlreadyExists()));

                const hashPassword = generateHashString(password!.trim());
                const activationCode = generateActivationCode();
                userRepo.createNewUser(email!.trim().toLowerCase(), hashPassword, firstName!.trim(), lastName, activationCode)
                    .then(newID => {
                        try {
                            mailService.sendEmailUserActivation(email!, activationCode);
                            const taskList = taskListService.createDefaultList(newID)
                            taskListService.createTaskList(taskList.listName, taskList.listDescription, newID)
                                .then(newList => { return resolve(null); })
                                .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
                        } catch (error) {
                            console.log(error);
                            return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage()));
                        }
                    })
                    .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
            })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
    })
}

const activateUser = (email: string | null, activationCode: string | null): Promise<UserDTO> => {
    return new Promise((resolve, reject) => {
        if (!isEmailValid(email) || activationCode == null || activationCode.trim().length !== LENGTH_ACTIVATION_CODE)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        userRepo.getUserByEmail(email!.trim().toLowerCase())
            .then(user => {
                if (user === null) return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
                const usr = (user as User);
                userRepo.activateUser(usr.email, activationCode.trim())
                    .then(() => {
                        usr.userStatus = STATUS_ACTIVE
                        return resolve(convertUserToDTO(usr));
                    })
                    .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
            })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
    })
}

const requestUserActivation = (email: string | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (!isEmailValid(email))
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        userRepo.getUserByEmail(email!.trim().toLowerCase())
            .then(user => {
                if (user === null) return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
                const activationCode = generateActivationCode();
                userRepo.updateUserActivationCode(email!.trim().toLowerCase(), activationCode)
                    .then(() => {
                        try {
                            mailService.sendEmailUserActivation(email!, activationCode);
                            return resolve(null);
                        } catch (error) {
                            console.log(error);
                            return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage()));
                        }
                    })
                    .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
            })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())); })
    })
}

const validateLoginData = (email: string | null, password: string | null): boolean => {
    return isEmailValid(email) && isPasswordParametersValid(password);
}

const validateNewUserData = (email: string | null, password: string | null, firstName: string | null, _lastName: string | null): boolean => {
    return isEmailValid(email) && isPasswordParametersValid(password) && firstName !== null && firstName.trim().length > 0;
}

const validateResetPasswordInput = (email: string | null, newPasswordCode: string | null, newPassword: string | null) => {
    return isEmailValid(email) && newPasswordCode !== null || newPasswordCode!.trim().length === LENGTH_NEW_PASSWORD_CODE && isPasswordParametersValid(newPassword)
}

const generateActivationCode = () => {
    return generateRandomString(LENGTH_ACTIVATION_CODE);
}

const generateNewPasswordCode = () => {
    return generateRandomString(LENGTH_NEW_PASSWORD_CODE);
}

const generateRandomString = (length: number) => {
    const chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
    let result = '';
    for (let i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
}

const generateHashString = (str: string) => {
    return bcrypt.hashSync(str, saltRounds);
}

export default {
    login,
    recoverPassword,
    resetPassword,
    registerNewUser,
    activateUser,
    requestUserActivation,
    generateHashString
}
