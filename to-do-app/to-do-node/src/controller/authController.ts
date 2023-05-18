import { Request, Response } from "express";
import msg from '../business/messages/messages';
import { Error, AppErrorType } from "../business/errorType";
import authService from '../business/authService';

const login = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    authService.login(req.body.email, req.body.password)
        .then(userDTO => res.status(200).json(userDTO))
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const recoverPassword = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    authService.recoverPassword(req.body.email)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const resetPassword = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    authService.resetPassword(req.body.email, req.body.newPasswordCode, req.body.password)
        .then(userDTO => res.status(200).json(userDTO))
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const registerNewUser = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    authService.registerNewUser(req.body.email, req.body.password, req.body.firstName, req.body.lastName)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const activateUser = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    authService.activateUser(req.body.email, req.body.activationCode)
        .then(userDTO => res.status(200).json(userDTO))
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const requestUserActivation = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    authService.requestUserActivation(req.body.email)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

export default {
    login,
    recoverPassword,
    resetPassword,
    registerNewUser,
    activateUser,
    requestUserActivation
}