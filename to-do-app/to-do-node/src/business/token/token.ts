import jwt from 'jsonwebtoken';
import { User } from '../../model/user';
import { Request } from 'express';

const SECRET = process.env.SECRET_KEY ? process.env.SECRET_KEY : "07J0PpNnASqADC9LVooxIVREQs6jUq0yyvVpGVQXJkAPtwLZ7cMQSiBbT9aSv8O1n7q3YKbW2l1niOWCbcz3ng==";
const EXP_TIME = 2 * 60 * 60 // 2 horas

const AUTH: string = "AUTH"

const generateToken = (user: User) => {
    let token = jwt.sign({
        AUTHORIZED: true,
        exp: Date.now() + EXP_TIME,
        USER_ID: user.id,
        EMAIL: user.email,
        FIRST_NAME: user.firstName,
        LAST_NAME: user.lastName,
        STATUS: user.userStatus,
    }, SECRET)
    return token;
}

const getUserData = (tokenString: string): UserData => {
    const decodedToken = jwt.verify(tokenString, SECRET) as UserDataToken;
    return {
        userID: decodedToken.USER_ID,
        email: decodedToken.EMAIL,
        firstName: decodedToken.FIRST_NAME,
        lastName: decodedToken.LAST_NAME,
        status: decodedToken.STATUS
    };
}

const refreshToken = (tokenString: string) => {
    const userData = getUserData(tokenString);
    const user = new User(
        userData.userID,
        userData.firstName,
        userData.lastName,
        userData.email,
        null,
        null,
        null,
        userData.status
    )
    return generateToken(user)
}

interface UserDataToken {
    AUTHORIZED: boolean,
    exp: number,
    USER_ID: number,
    EMAIL: string,
    FIRST_NAME: string,
    LAST_NAME: string | null,
    STATUS: number,
}

export interface UserData {
    userID: number,
    email: string,
    firstName: string,
    lastName: string | null,
    status: number
}

const getJwt = (req: Request) => {
    const jwt = req.header(AUTH);
    if (!jwt) return null
    return jwt;
}

const getUserID = (req: Request) => {
    const jwt = req.header(AUTH);
    if (!jwt) return null
    const userData = getUserData(jwt)
    return userData.userID
}

export default {
    AUTH,
    EXP_TIME,
    generateToken,
    getUserData,
    refreshToken,
    getUserID,
    getJwt
}