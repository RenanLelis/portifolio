package com.renan.todo.service

import java.security.MessageDigest
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import org.springframework.stereotype.Service

/**
 * Service for general utilities
 */
@Service
class UtilServiceImpl : UtilService {

    /**
     * Check if the string has email format
     */
    override fun isMail(str: String?): Boolean {
        if (!str.isNullOrEmpty()) {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = str.let { pattern.matcher(it) }
            return matcher.matches()
        }
        return false
    }

    /**
     * Generate random string with fixed size
     */
    override fun generateRandomString() = generateRandomString(LENGTH_STRING_RANDOM)

    /**
     * Generate a random string on the specified size
     */
    override fun generateRandomString(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
        val random = Random()
        val result = StringBuilder()
        for (i in 0 until length) {
            val r = random.nextInt(chars.size - 1)
            result.append(chars[r])
        }
        return result.toString()
    }

    /**
     * Check if the string is in a valid date format
     */
    override fun isValidDate(dateStr: String?): Boolean {
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

    /**
     * Generate a byte[] with the String's hash
     */
    override fun generateHash(text: String): ByteArray {
        val md = MessageDigest.getInstance(HASH_ALGORITHM)
        md.update(text.toByteArray())
        return md.digest()
    }

    /**
     * Generate a String with the text's hash
     */
    override fun generateHashString(text: String) = stringHex(generateHash(text))

    /**
     * Convert a byte array to string
     */
    private fun stringHex(bytes: ByteArray): String {
        val s = java.lang.StringBuilder()
        for (aByte in bytes) {
            val highPart = aByte.toInt() shr 4 and 0xf shl 4
            val lowPart = aByte.toInt() and 0xf
            if (highPart == 0) s.append('0')
            s.append(Integer.toHexString(highPart or lowPart))
        }
        return s.toString()
    }


}