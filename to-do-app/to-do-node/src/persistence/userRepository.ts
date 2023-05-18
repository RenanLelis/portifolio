import { MysqlError } from 'mysql';
import sql from './db'
import { User } from '../model/user';
import { STATUS_ACTIVE, STATUS_INACTIVE } from '../business/consts';

const getUserByEmail = (email: string): Promise<User | null> => {
    return new Promise((resolve, reject) => {
        sql.query(`SELECT ID, EMAIL, USER_PASSWORD, FIRST_NAME, LAST_NAME, USER_STATUS, 
        ACTIVATION_CODE, NEW_PASSWORD_CODE FROM USER_TODO WHERE EMAIL = ?`,
            [email],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                if (results !== null && results.length > 0) {
                    const user: User = new User(
                        results[0].ID,
                        results[0].FIRST_NAME,
                        results[0].LAST_NAME,
                        results[0].EMAIL,
                        results[0].USER_PASSWORD,
                        results[0].ACTIVATION_CODE,
                        results[0].NEW_PASSWORD_CODE,
                        results[0].USER_STATUS,
                    )
                    return resolve(user);
                }
                return resolve(null);
            });
    })
}

const activateUser = (email: string, activationCode: string): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE USER_TODO SET ACTIVATION_CODE = null, 
        USER_STATUS = ? WHERE EMAIL = ? AND ACTIVATION_CODE = ?`,
            [STATUS_ACTIVE, email, activationCode],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const updatePasswordByCode = (email: string, newPasswordCode: string, hashPassword: string): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE USER_TODO SET USER_PASSWORD = ?, NEW_PASSWORD_CODE = null, 
        USER_STATUS = ?, ACTIVATION_CODE = null WHERE EMAIL = ? AND NEW_PASSWORD_CODE = ?`,
            [hashPassword, STATUS_ACTIVE, email, newPasswordCode],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const updateUserNewPasswordCode = (email: string, newPasswordCode: string): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE USER_TODO SET NEW_PASSWORD_CODE = ? WHERE EMAIL = ?`,
            [newPasswordCode, email],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const createNewUser = (email: string, hashPassword: string, firstName: string, lastName: string | null, activationCode: string): Promise<number> => {
    return new Promise((resolve, reject) => {
        sql.query(`INSERT INTO USER_TODO 
        (EMAIL, USER_PASSWORD, FIRST_NAME, LAST_NAME, ACTIVATION_CODE, USER_STATUS)
        VALUES (?, ?, ?, ?, ?, ?)`,
            [email, hashPassword, firstName, lastName, activationCode, STATUS_INACTIVE],
            (err, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(results.insertId);
            });
    });
}

const updateUserActivationCode = (email: string, activationCode: string): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE USER_TODO SET ACTIVATION_CODE = ? WHERE EMAIL = ?`,
            [activationCode, email],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const updatePasswordById = (id: number, hashPassword: string): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE USER_TODO SET USER_PASSWORD = ? WHERE ID = ?`,
            [hashPassword, id],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const UpdateUser = (id: number, firstName: string, lastName: string | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE USER_TODO SET FIRST_NAME = ?, LAST_NAME = ? WHERE ID = ?`,
            [firstName, lastName, id],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

export default {
    getUserByEmail,
    activateUser,
    updatePasswordByCode,
    updateUserNewPasswordCode,
    createNewUser,
    updateUserActivationCode,
    updatePasswordById,
    UpdateUser
}
