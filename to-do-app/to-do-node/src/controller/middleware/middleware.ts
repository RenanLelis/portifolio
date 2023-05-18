import { NextFunction, Request, Response } from "express"
import token from "../../business/token/token"
import { AppErrorType } from "../../business/errorType"
import msg from "../../business/messages/messages"
import { UserDTO } from "../../business/dto/dto"

const validateAuth = (req: Request, res: Response, next: NextFunction) => {
    const jwt = token.getJwt(req)
    if (!jwt || jwt === null)
        return res.status(AppErrorType.NOT_ALLOWED).json({ errorMessage: msg.getErrorMessageToken() })

    try {
        const userData = token.getUserData(jwt)
        const newUserData = new UserDTO(
            token.refreshToken(jwt),
            token.EXP_TIME,
            userData.userID,
            userData.firstName,
            userData.lastName,
            userData.email,
            userData.status
        )
        res.setHeader(token.AUTH, JSON.stringify(newUserData))
        next();
    } catch (error) {
        return res.status(AppErrorType.NOT_ALLOWED).json({ errorMessage: msg.getErrorMessageToken() })
    }
}

export default {
    validateAuth,
}