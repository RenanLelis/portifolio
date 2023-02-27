const msg = require('../util/message');

const login = (email, password) => {
    return new Promise((resolve, reject) => {
        if (!validateLoginInput(email, password)) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        //TODO
    })
}

const getNewPasswordCode = (email) => {
    return new Promise((resolve, reject) => {
        if (!email || !validator.validate(email)) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        //TODO
    })
}

const registerNewPassword = (email, newPassword, passCode) => {
    return new Promise((resolve, reject) => {
        if (!email || !passCode || !validator.validate(email) || passCode.trim().length !== 6 || !newPassword) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        //TODO
    })
}

const registerUser = (email, userPassword, userName, lastName) => {
    return new Promise((resolve, reject) => {
        if (!email || !userPassword || !validator.validate(email) || !userName) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        //TODO
    })
}

const activateUser = (email, activationCode) => {
    return new Promise((resolve, reject) => {
        if (!email || !activationCode || !validator.validate(email) || activationCode.trim().length !== 6) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        //TODO
    })
}

//Validade the input data for login
const validateLoginInput = (email, password) => {
    return validator.validate(email) && password != null && password.length > 5;
}

module.exports = { login, getNewPasswordCode, registerNewPassword, registerUser, activateUser }