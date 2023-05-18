import { Request, Response } from "express"
import msg from '../business/messages/messages';
import { Error, AppErrorType } from "../business/errorType";
import userService from '../business/userService';
import token from '../business/token/token'

const updateUserProfile = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req)
    userService.updateUserProfile(userID, req.body.firstName, req.body.lastName)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const updateUserPassword = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req)
    userService.updateUserPassword(userID, req.body.password)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}


export default {
    updateUserProfile,
    updateUserPassword
}