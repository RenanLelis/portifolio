import mailer from 'nodemailer'
import SMTPTransport from 'nodemailer/lib/smtp-transport';

const mailConfig = {
    MAIL_FROM: process.env.MAIL_FROM,
    MAIL_PASSWORD: process.env.MAIL_PASSWORD,
    MAIL_SMTP_HOST: process.env.MAIL_SMTP_HOST,
    MAIL_SMTP_PORT: process.env.MAIL_SMTP_PORT,
    SSL: true
}

const sendEmailRecoverPassword = (email: string, newPasswordCode: string) => {
    const title = "TODO - Recover your password";
    const message = generateMessageNewPassword(newPasswordCode);
    sendMail(email, title, message);
}

const sendEmailUserActivation = (email: string, activationCode: string) => {
    const title = "TODO - Recover your password";
    const message = generateMessageUserActivation(activationCode);
    sendMail(email, title, message);
}

const sendMail = (email: string, title: string, message: string) => {

    const transporter = mailer.createTransport({
        host: mailConfig.MAIL_SMTP_HOST ? mailConfig.MAIL_SMTP_HOST : "",
        port: mailConfig.MAIL_SMTP_PORT,
        secure: mailConfig.SSL,
        auth: {
            user: mailConfig.MAIL_FROM,
            pass: mailConfig.MAIL_PASSWORD
        },
    } as SMTPTransport.Options);

    const mail = {
        from: mailConfig.MAIL_FROM,
        to: email,
        subject: title,
        html: message,
    }

    return new Promise((resolve, reject) => {
        transporter.sendMail(mail)
            .then(response => {
                transporter.close();
            })
            .catch(error => {
                transporter.close();
                throw error;
            });
    })

}

const generateMessageNewPassword = (newPasswordCode: string) => {
    return `<div style=" text-align:center; width:450px; margin:10px auto; border-radius:10px;">
    <h1 style="color:#555555; font-size: 20px;">TODO</h1>
    <p style="color:#555555; font-size: 16px; padding:10px;">To generate a new password use the code below</p>
    <p style="color:#555555; font-size: 16px;"> ${newPasswordCode} </p></div>`
}

const generateMessageUserActivation = (activationCode: string) => {
    return `<div style=" text-align:center; width:450px; margin:10px auto; border-radius:10px;">
    <h1 style="color:#555555; font-size: 20px;">TODO</h1>
    <p style="color:#555555; font-size: 16px; padding:10px;">To generate activate your access on the system use the code below</p>
    <p style="color:#555555; font-size: 16px;"> ${activationCode} </p></div>`
}

export default {
    sendEmailRecoverPassword,
    sendEmailUserActivation
}
