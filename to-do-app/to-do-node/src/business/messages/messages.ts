const MSE01 = "MSE01" // An error ocurred on the operation.
const MSE02 = "MSE02" // Error on values properties.
const MSE03 = "MSE03" // Invalid Token.
const MSE04 = "MSE04" // Invalid User or Password.
const MSE05 = "MSE05" // E-mail already exists.
const MSE06 = "MSE06" // E-mail not found.
const MSE07 = "MSE07" // User not activated.
const MSE08 = "MSE08" // Cannot delete only list for the user.

const getErrorMessage = () => MSE01
const getErrorMessageInputValues = () => MSE02
const getErrorMessageToken = () => MSE03
const getErrorMessageLogin = () => MSE04
const getErrorMessageEmailAlreadyExists = () => MSE05
const getErrorMessageUserNotFound = () => MSE06
const getErrorMessageUserNotActive = () => MSE07
const getErrorMessageCannotDeleteOnlyList = () => MSE08

export default {
    getErrorMessage,
    getErrorMessageInputValues,
    getErrorMessageToken,
    getErrorMessageLogin,
    getErrorMessageEmailAlreadyExists,
    getErrorMessageUserNotFound,
    getErrorMessageUserNotActive,
    getErrorMessageCannotDeleteOnlyList
}