const User = require("../model/user");
const sql = require("./db.js");

const getUserByEmail = (email, result) => {
    sql.query(`SELECT ID, EMAIL, USER_PASSWORD, USER_NAME, LAST_NAME, USER_STATUS
     FROM USER WHERE EMAIL = ?`, [email], (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(err, null);
            return;
        }
        if (res.length) {
            let user = new User(
                res[0].ID,
                res[0].EMAIL,
                res[0].USER_PASSWORD,
                null,
                null,
                res[0].USER_NAME,
                res[0].LAST_NAME,
                res[0].USER_STATUS
            );
            result(null, user);
            return;
        }
        result(null, null);
    });
}

const registerUser = (email, userPassword, userName, lastName, activationCode, result) => {
    sql.query(`INSERT INTO USER (EMAIL, USER_PASSWORD, USER_NAME, LAST_NAME, USER_STATUS, ACTIVATION_CODE)
    		 VALUES (?, ?, ?, ?, ?, ?)`,
        [email, userPassword, userName, lastName, User.STATUS_INACTIVE, activationCode],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, { id: res.insertId });
        }
    );
}

const activateUser = (email, activationCode, result) => {
    sql.query(`UPDATE USER SET USER_STATUS = ?, ACTIVATION_CODE = null WHERE EMAIL = ? AND ACTIVATION_CODE = ?`,
        [User.STATUS_ACTIVE, email, activationCode],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

const setNewPasswordCode = (newPasswordCode, email, result) => {
    sql.query(`UPDATE USER SET NEW_PASSWORD_CODE = ? WHERE EMAIL = ?`,
        [newPasswordCode, email],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

const registerNewPassword = (email, newPassword, passCode, result) => {
    sql.query(`UPDATE USER SET USER_PASSWORD = ?, NEW_PASSWORD_CODE = null WHERE EMAIL = ? AND NEW_PASSWORD_CODE = ?`,
        [newPassword, email, newPassword],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

const updateUserPassword = (id, newPassword, result) => {
    sql.query(
        "UPDATE USER SET USER_PASSWORD = ? WHERE ID = ?",
        [newPassword, id],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

module.exports = {
    getUserByEmail,
    registerUser,
    activateUser,
    setNewPasswordCode,
    registerNewPassword,
    updateUserPassword
}