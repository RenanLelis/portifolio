module.exports = class User {

    static STATUS_ACTIVE = 1;
    static STATUS_INACTIVE = 0;

    constructor(id, email, userPassword, newPasswordCode, activationCode, userName, lastName, userStatus) {
        this.id = id;
        this.email = email;
        this.userPassword = userPassword;
        this.newPasswordCode = newPasswordCode;
        this.activationCode = activationCode;
        this.userName = userName;
        this.lastName = lastName;
        this.userStatus = userStatus;
    }
}
