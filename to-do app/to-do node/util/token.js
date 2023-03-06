var jwt = require('jsonwebtoken');

const SECRET = process.env.SECRET ? process.env.SECRET : "07J0PpNnASqADC9LVooxIVREQs6jUq0yyvVpGVQXJkAPtwLZ7cMQSiBbT9aSv8O1n7q3YKbW2l1niOWCbcz3ng==";
const EXP_TIME = 2 * 60 * 60 // 2 horas

const generateToken = (user) => {
    var token = jwt.sign({
        authorized: true,
        userID: user.id,
        status: user.status
    }, SECRET, { expiresIn: EXP_TIME });
    return token;
}

const getUserData = (tokenString) => {
    try {
        var decodedToken = jwt.verify(tokenString, SECRET);
        return { userID: decodedToken.userID, status: decodedToken.status };
    } catch (err) {
        throw err;
    }
}

const refreshToken = (tokenString) => {
    try {
        var decodedToken = jwt.verify(tokenString, SECRET);
        var newToken = jwt.sign({
            authorized: true,
            userID: decodedToken.userID,
            status: decodedToken.status
        }, SECRET, { expiresIn: EXP_TIME });
        return { token: newToken, userID: decodedToken.userID, status: decodedToken.status };
    } catch (err) {
        /* error object example
        err = {
            name: 'TokenExpiredError',
            message: 'jwt expired',
            expiredAt: 1408621000
        }
        */
        throw err;
    }
}


module.exports = {
    generateToken, refreshToken, getUserData, EXP_TIME,
}