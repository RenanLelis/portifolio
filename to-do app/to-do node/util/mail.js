const mailer = require("nodemailer");

const mailConfig = {
    EMAIL_SENDER: process.env.EMAIL_SENDER ? process.env.EMAIL_SENDER : "youremail@gmail.com",
    EMAIL_PASSWORD: process.env.EMAIL_PASSWORD ? process.env.EMAIL_PASSWORD : "thePasswordProvided",
    SMTP_HOST: process.env.SMTP_HOST ? process.env.SMTP_HOST : "smtp.gmail.com",
    SMTP_PORT: process.env.SMTP_PORT ? process.env.SMTP_PORT : 465,
    SSL: true
}

const SUBJECT_NEW_PASSWORD = "New Password Code - ToDo App"
const SUBJECT_ACTIVATE_USER = "User Activation Code - ToDo App"

const sendEmail = (to, subject, message) => {

    const smtpTransport = mailer.createTransport({
        host: mailConfig.SMTP_HOST,
        port: mailConfig.SMTP_PORT,
        secure: mailConfig.SSL,
        auth: {
            user: mailConfig.EMAIL_SENDER,
            pass: mailConfig.EMAIL_PASSWORD
        }
    })

    const mail = {
        from: mailConfig.EMAIL_SENDER,
        to: to,
        subject: subject,
        html: message,
    }

    return new Promise((resolve, reject) => {
        smtpTransport.sendMail(mail)
            .then(response => {
                smtpTransport.close();
                return resolve(response);
            })
            .catch(error => {
                smtpTransport.close();
                return reject(error);
            });
    })

}

const sendActivationUserEmail = (to, activationCode) => {
    return sendEmail(to, SUBJECT_ACTIVATE_USER, generateUserActivationMessage(activationCode));
}

const generateUserActivationMessage = (activationCode) => {
    let message = `<div style=" text-align:center; width:450px; margin:10px auto; border-radius:10px;">
    <h1 style="color:#b67509; font-size: 20px;">ToDo App</h1>
    <p style="color:#b67509; font-size: 16px; padding:10px; ">To activate your user use the following code</p>
    <p style=\"color:#b67509; font-size: 16px;\">  ${activationCode}  </p></div>`
    return message
}

const sendNewPasswordEmail = (to, newPasswordCode) => {
    return sendEmail(to, SUBJECT_NEW_PASSWORD, generateNewPassowrdMessage(newPasswordCode));
}

const generateNewPassowrdMessage = (newPasswordCode) => {
    let message = `<div style=" text-align:center; width:450px; margin:10px auto; border-radius:10px;">
    <h1 style="color:#b67509; font-size: 20px;">ToDo App</h1>
    <p style="color:#b67509; font-size: 16px; padding:10px; ">To generate a new password use the following code</p>
    <p style=\"color:#b67509; font-size: 16px;\">  ${newPasswordCode}  </p></div>`
    return message
}

module.exports = { sendNewPasswordEmail, sendActivationUserEmail, sendEmail }