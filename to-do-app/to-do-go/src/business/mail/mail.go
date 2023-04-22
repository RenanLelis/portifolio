package mail

import (
	"crypto/tls"

	gomail "gopkg.in/mail.v2"
	"renanlelis.github.io/portfolio/to-do-go/src/config"
)

// SendEmailRecoverPassword send email with new password code
func SendEmailRecoverPassword(email, newPasswordCode string) error {
	title := "TODO - Recover your password"
	message := generateMessageNewPassword(newPasswordCode)
	return sendEmail(email, message, title)
}

// SendEmailUserActivation send email with activation code
func SendEmailUserActivation(email, activationCode string) error {
	title := "TODO - User Activation"
	message := generateMessageUserActivation(activationCode)
	return sendEmail(email, message, title)
}

// sendEmail send email to user with message and title as param
func sendEmail(email, message, title string) error {
	m := gomail.NewMessage()
	m.SetHeader("From", config.SystemEmail)
	m.SetHeader("To", email)
	m.SetHeader("Subject", title)
	m.SetBody("text/html", message)
	d := gomail.NewDialer(config.SmtpHost, config.SmtpPort, config.SystemEmail, config.MailPassword)
	d.TLSConfig = &tls.Config{InsecureSkipVerify: true}
	if err := d.DialAndSend(m); err != nil {
		return err
	}
	return nil
}

// generateMessageNewPassword generate the message for new password
func generateMessageNewPassword(newPasswordCode string) string {
	message := "<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
	message = message + "<h1 style=\"color:#555555; font-size: 20px;\">TODO</h1>"
	message = message + "<p style=\"color:#555555; font-size: 16px; padding:10px; \">To generate a new password use the code below</p>"
	message = message + "<p style=\"color:#555555; font-size: 16px;\"> " + newPasswordCode + " </p>" + "</div>"
	return message
}

// generateMessageUserActivation generate the message for user activation
func generateMessageUserActivation(activationCode string) string {
	message := "<div style=\" text-align:center; width:450px; margin:10px auto; border-radius:10px;\">"
	message = message + "<h1 style=\"color:#555555; font-size: 20px;\">TODO</h1>"
	message = message + "<p style=\"color:#555555; font-size: 16px; padding:10px; \">To generate activate your access on the system use the code below</p>"
	message = message + "<p style=\"color:#555555; font-size: 16px;\"> " + activationCode + " </p>" + "</div>"
	return message
}
