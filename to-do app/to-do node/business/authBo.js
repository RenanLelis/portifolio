const msg = require('../util/message');
const userRepo = require('../persistence/userRepository');
const util = require('../util/util');
const bcrypt = require('bcrypt');
const mailSender = require('../util/mail');
const User = require('../model/user');

const login = (email, password) => {
    return new Promise((resolve, reject) => {
        if (!validateLoginInput(email, password)) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        userRepo.getUserByEmail(email, (error, result) => {
            if (error != null) {
                return reject({ error: msg.getErrorMessage(), status: 500 });
            }
            if (result == null) {
                return reject({ error: msg.getErrorMessageLogin(), status: 400 });
            }
            bcrypt.compare(password, result.userPassword, function (error, res) {
                if (error != null) {
                    return reject({ error: msg.getMensagemErro(), status: 500 });
                }
                if (res == false) {
                    return reject({ error: msg.getMensagemErroLogin(), status: 400 });
                }
                return resolve(result);
            });
        })
    })
}

const getNewPasswordCode = (email) => {
    return new Promise((resolve, reject) => {
        if (!email || !validator.validate(email)) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        const newPasswordCode = util.generateNewPasswordCode();
        userRepo.setNewPasswordCode(newPasswordCode, email, (error, result) => {
            if (error != null) {
                return reject({ error: msg.getErrorMessage(), status: 500 });
            }
            mailSender.sendNewPasswordEmail(email, newPasswordCode).then(
                (value) => {
                    return resolve();
                },
                (error) => {
                    return reject({ erro: msg.getMensagemErro(), status: 500 });
                });
        });
    })
}

const registerNewPassword = (email, newPassword, passCode) => {
    return new Promise((resolve, reject) => {
        if (!email || !passCode || !validator.validate(email) || passCode.trim().length !== 6 || !newPassword) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        const hashPassword = bcrypt.hashSync(newPassword, 10);
        userRepo.registerNewPassword(email, hashPassword, passCode, (erro, result) => {
            if (erro != null) {
                return reject({ erro: msg.getMensagemErro(), status: 500 });
            }
            return resolve();
        })
    })
}

const registerUser = (email, userPassword, userName, lastName) => {
    return new Promise((resolve, reject) => {
        if (!email || !userPassword || !validator.validate(email) || !userName) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        

        userRepo.getUserByEmail(email, (error, result) => {
            if (error !== null) {
                return reject({ error: msg.getErrorMessage(), status: 500 });
            }
            if (result !== null) {
                return reject({ error: msg.getErrorMessageEmailAlreadyExists(), status: 400 });
            }
            const activationCode = util.generateActivationCode();
            const hashPassword = bcrypt.hashSync(newPassword, 10);
            const user = new User(null, email, hashPassword, null, null, userName, lastName, User.STATUS_INACTIVE);
            userRepo.registerUser(email, hashPassword, userName, 
                lastName !== null && lastName.trim().length > 0 ? lastName.trim() : null, 
                activationCode, 
                (error, result) => {
                    if (error != null) {
                        return reject({ error: msg.getErrorMessage(), status: 500 });
                    }
                    user.id = result.id;
                    return resolve(user);
            });
        });
    })
}

const activateUser = (email, activationCode) => {
    return new Promise((resolve, reject) => {
        if (!email || !activationCode || !validator.validate(email) || activationCode.trim().length !== 6) {
            return reject({ erro: msg.getErrorMessageInputValues(), status: 400 });
        }
        userRepo.getUserByEmail(email, (error, result) => {
            if (error != null) {
                return reject({ error: msg.getErrorMessage(), status: 500 });
            }
            if (result == null) {
                return reject({ error: msg.getErrorMessageUserNotFound(), status: 400 });
            }
            let user = result;
            user.activationCode = null;
            user.userStatus = User.STATUS_ACTIVE
            userRepo.activateUser(email, activationCode, (error, result) => {
                if (error != null) {
                    return reject({ error: msg.getErrorMessage(), status: 500 });
                }
                return resolve(user);
            })
        })
    })
}

//Validade the input data for login
const validateLoginInput = (email, password) => {
    return validator.validate(email) && password != null && password.length > 5;
}

module.exports = { login, getNewPasswordCode, registerNewPassword, registerUser, activateUser }