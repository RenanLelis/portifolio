const msg = require('../util/message');
const jwtGen = require('../util/token');
const authBo = require('../business/authBo');

//login for the user
const login = (req, res) => {
    if (!req.body || !req.body.email || !req.body.password) {
        return res.status(400).json({
            error: msg.getErrorMessageInputValues()
        });
    }
    authBo.login(req.body.email, req.body.password).then(
        (user) => {
            let token = jwtGen.generateToken(user)
            let returnValue = {
                id: user.id,
                email: user.email,
                jwt: token,
                name: user.name,
                lastName: user.lastName ? user.lastName : "",
                status: user.status,
                expiresIn: (jwtGen.EXP_TIME * 1000)
            }
            return res.status(200).json(returnValue);
        },
        (error) => {
            if (error.status && error.error) {
                return res.status(error.status).json({ error: error.error });
            }
            return res.status(500).json({ error: msg.getErrorMessage() });
        }
    );
}

//For the user that forget the password
const getNewPasswordCode = (req, res) => {
    if (!req.body || !req.body.email || !validator.validate(email)) {
        return res.status(400).json({
            error: msg.getErrorMessageInputValues()
        });
    }
    authBo.getNewPasswordCode(req.body.email).then(
        (value) => {
            return res.status(200).send();
        },
        (error) => {
            if (error.status && error.error) {
                return res.status(error.status).json({ error: error.error });
            }
            return res.status(500).json({ error: msg.getErrorMessage() });
        }
    );
}

//Register the new password for the user using the code sent
const registerNewPassword = (req, res) => {
    if (!req.body || !req.body.email || !req.body.password || !req.body.newPasswordCode) {
        return res.status(400).json({
            error: msg.getErrorMessageInputValues()
        });
    }
    authBo.registerNewPassword(req.body.email, req.body.password, req.body.newPasswordCode).then(
        (value) => {
            return res.status(200).send();
        },
        (error) => {
            console.log(error)
            if (error.status && error.error) {
                return res.status(error.status).json({ error: error.error });
            }
            return res.status(500).json({ error: msg.getErrorMessage() });
        }
    );
}

//Register a new user on the database
const registerUser = (req, res) => {
    if (!req.body || !req.body.email || !req.body.password || !req.body.userName) {
        return res.status(400).json({
            error: msg.getErrorMessageInputValues()
        });
    }
    authBo.registerUser(req.body.email, req.body.password, req.body.userName, req.body.lastName).then(
        (user) => {
            let token = jwtGen.generateToken(user)
            let returnValue = {
                id: user.id,
                email: user.email,
                jwt: token,
                name: user.name,
                lastName: user.lastName ? user.lastName : "",
                status: user.status,
                expiresIn: (jwtGen.EXP_TIME * 1000)
            }
            return res.status(201).json(returnValue);
        },
        (error) => {
            console.log(error)
            if (error.status && error.error) {
                return res.status(error.status).json({ error: error.error });
            }
            return res.status(500).json({ error: msg.getErrorMessage() });
        }
    );
}

//Activate a user using the code sent
const activateUser = (req, res) => {
    if (!req.body || !req.body.email || !req.body.activationCode) {
        return res.status(400).json({
            error: msg.getErrorMessageInputValues()
        });
    }
    authBo.activateUser(req.body.email, req.body.activationCode).then(
        (user) => {
            let token = jwtGen.generateToken(user)
            let returnValue = {
                id: user.id,
                email: user.email,
                jwt: token,
                name: user.name,
                lastName: user.lastName ? user.lastName : "",
                status: user.status,
                expiresIn: (jwtGen.EXP_TIME * 1000)
            }
            return res.status(200).json(returnValue);
        },
        (error) => {
            console.log(error)
            if (error.status && error.error) {
                return res.status(error.status).json({ error: error.error });
            }
            return res.status(500).json({ error: msg.getErrorMessage() });
        }
    );
}

module.exports = { login, getNewPasswordCode, registerNewPassword, registerUser, activateUser }