package com.renan.todo.service

const val DATE_FORMAT = "yyyy-MM-dd"
const val LENGTH_STRING_RANDOM = 6
const val HASH_ALGORITHM = "SHA-256"

/**
 * Service for general utilities
 */
interface UtilService {

    /**
     * Check if the string has email format
     */
    fun isMail(str: String?): Boolean

    /**
     * Generate random string with fixed size
     */
    fun generateRandomString(): String

    /**
     * Generate a random string on the specified size
     */
    fun generateRandomString(length: Int): String

    /**
     * Check if the string is in a valid date format
     */
    fun isValidDate(dateStr: String?): Boolean

    /**
     * Generate a byte[] with the String's hash
     */
    fun generateHash(text: String): ByteArray

    /**
     * Generate a String with the text's hash
     */
    fun generateHashString(text: String): String

}