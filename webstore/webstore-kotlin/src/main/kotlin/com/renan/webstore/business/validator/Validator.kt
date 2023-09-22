package com.renan.webstore.business.validator

import com.renan.webstore.model.LENGTH_ACTIVATION_CODE
import com.renan.webstore.model.LENGTH_NEW_PASSWORD_CODE
import com.renan.webstore.model.MAX_LENGTH_PASSWORD
import com.renan.webstore.model.MIN_LENGTH_PASSWORD
import org.springframework.stereotype.Component
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

const val DATE_FORMAT = "yyyy-MM-dd"

@Component
class Validator {

    fun isMail(str: String?): Boolean {
        if (!str.isNullOrEmpty()) {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = str.let { pattern.matcher(it) }
            return matcher.matches()
        }
        return false
    }

    fun isValidDate(dateStr: String?): Boolean {
        val sdf: DateFormat = SimpleDateFormat(DATE_FORMAT)
        sdf.isLenient = false
        try {
            sdf.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun isValidPassword(password: String?): Boolean {
        var valid = !password.isNullOrBlank()
        val formattedPassword = if (valid) password!!.trim() else ""
        valid =
            valid && formattedPassword.length >= MIN_LENGTH_PASSWORD && formattedPassword.length <= MAX_LENGTH_PASSWORD
        if (valid) {
            var capitalFlag = false
            var lowerCaseFlag = false
            var numberFlag = false
            for (ch in formattedPassword) {
                if (Character.isDigit(ch)) {
                    numberFlag = true
                } else if (Character.isUpperCase(ch)) {
                    capitalFlag = true
                } else if (Character.isLowerCase(ch)) {
                    lowerCaseFlag = true
                }
                if (numberFlag && capitalFlag && lowerCaseFlag) return true
            }
        }
        return false
    }

    fun isValidNewPasswordCode(newPasswordCode: String?) =
        !newPasswordCode.isNullOrBlank() &&
                newPasswordCode.trim().length == LENGTH_NEW_PASSWORD_CODE

    fun isValidActivationCode(activationCode: String?) =
        !activationCode.isNullOrBlank() &&
                activationCode.trim().length == LENGTH_ACTIVATION_CODE

}