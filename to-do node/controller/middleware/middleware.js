const jwtGen = require('../../util/token');
const msg = require('../../util/message');

const validateUserLoggedIn = (req, res, next) => {
    if (!req.headers.auth) {
        return res.status(401).json({
            erro: msg.getErrorMessageToken()
        });
    }
    try {
        let tokenData = jwtGen.refreshToken(req.headers.auth);
        res.setHeader("AUTH", JSON.stringify(tokenData));
        next();
    } catch (err) {
        return res.status(401).json({
            erro: msg.getErrorMessageToken()
        });
    }
}

module.exports = {
    validateUserLoggedIn
};