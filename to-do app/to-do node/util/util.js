const generateNewPasswordCode = () => {
    return randomString(6);
}

const generateActivationCode = () => {
    return randomString(6);
}

const randomString = (length) => {
    let chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
}

module.exports = {
    generateNewPasswordCode, generateActivationCode, randomString,
}