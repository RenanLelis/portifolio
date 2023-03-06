const MSE01 = "MSE01" // An error ocurred on the operation.
const MSE02 = "MSE02" // Error on values properties.
const MSE03 = "MSE03" // Invalid Token.
const MSE04 = "MSE04" // Invalid User or Password.
const MSE05 = "MSE05" // E-mail already exists.
const MSE06 = "MSE06" // E-mail not found.

//getErrorMessage return a generic error message
const getErrorMessage = () => {
    return MSE01
}

//getErrorMessageInputValues return a error message about the input values
const getErrorMessageInputValues = () => {
    return MSE02
}

//getErrorMessageToken return an error message about the invalid jwt token
const getErrorMessageToken = () => {
    return MSE03
}

//getErrorMessageLogin return an error message about invalid user or password
const getErrorMessageLogin = () => {
    return MSE04
}

//getErrorMessageEmailAlreadyExists return an error message about a email that already exists
const getErrorMessageEmailAlreadyExists = () => {
    return MSE05
}

//getErrorMessageUserNotFound return an error message about an email not found on database
const getErrorMessageUserNotFound = () => {
    return MSE06
}

module.exports = {
    getErrorMessage,
    getErrorMessageInputValues,
    getErrorMessageToken,
    getErrorMessageLogin,
    getErrorMessageEmailAlreadyExists,
    getErrorMessageUserNotFound
}
