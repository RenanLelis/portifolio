package com.renan.todo.util;

import java.math.BigDecimal;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitie class for String manipulation
 *
 * @author renan
 */
public class StringUtil {

    public static final String REGEX_DIGIT = "[0-9]";
    public static final String REGEX_NOT_DIGIT = "[^0-9]";
    public static final String REGEX_ALPHANUMERIC = "[a-zA-z_0-9]";
    public static final String REGEX_UPPERCASE = "[A-Z]";
    public static final String REGEX_LOWERCASE = "[a-z]";
    public static final String EMPTY_STRING = "";
    public static final String NULL_STRING = "null";
    public static final String REGEX_WHITE_SPACE = "[\\s]";
    public static final String SPACE_STRING = " ";
    public static final String REGEX_NOT_CHARACTERS = "[\\W&&[^\\s]]";
    public static final Integer LENGTH_STRING_RANDOM = 15;
    public static final Integer LENGTH_STRING_RANDOM_PASSWORD = 6;
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVYWXZabcdefghijklmnopqrstuvwxyz0123456789!@#$%ˆ&*()";

    /**
     * get the first char of the string
     *
     * @param element
     * @return
     */
    public static String getFirstCharacter(String element) {
        return StringUtil.isNotEmpty(element) ? element.substring(0, 1) : StringUtil.EMPTY_STRING;
    }

    /**
     * Check if the string is empty
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0 || str.trim().equals(StringUtil.EMPTY_STRING);
    }

    /**
     * Check if the string is not empty
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }

    /**
     * Get a string with the number parameter
     *
     * @param value
     * @return
     */
    public static String toString(BigDecimal value) {
        return value != null ? value.toString() : StringUtil.EMPTY_STRING;
    }

    /**
     * Returns a String of a number with zeroes at left
     *
     * @param numeroZeros
     * @param numeroReal
     * @return
     */
    public static String zerosErquerda(Integer numeroZeros, Integer numeroReal) {
        String zeros = "00000000000000000000000000000000000000000000000";
        String numero = numeroReal.toString();
        zeros = zeros.substring(0, numeroZeros - numero.length()) + numeroReal.toString();
        return zeros;
    }

    /**
     * Check if the string has digits
     *
     * @param str
     * @return
     */
    public static Boolean hasNumber(String str) {
        // create regular expression
        Pattern expression = Pattern.compile(StringUtil.REGEX_DIGIT);
        // match regular expression to string and print matches
        Matcher matcher = expression.matcher(str);
        return matcher.find();
    }

    /**
     * Check if the string only has digits
     *
     * @param str
     * @return
     */
    public static Boolean isNotOnlyNumber(String str) {
        // create regular expression
        Pattern expression = Pattern.compile(StringUtil.REGEX_NOT_DIGIT);
        // match regular expression to string and print matches
        Matcher matcher = expression.matcher(str);
        return matcher.find();
    }

    /**
     * Check if the string has email format
     *
     * @param str
     * @return
     */
    public static Boolean ismail(String str) {
        Boolean result = false;
        if (StringUtil.isNotEmpty(str)) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(str);
            result = matcher.matches();
        }
        return result;
    }

    /**
     * Check if the String has only digits
     *
     * @param str
     * @return
     */
    public static Boolean isOnlyNumber(String str) {
        return !StringUtil.isNotOnlyNumber(str);
    }

    /**
     * Check if the String has Alphanumerics
     *
     * @param str
     * @return
     */
    public static Boolean hasAlphanumeric(String str) {
        // create regular expression
        Pattern expression = Pattern.compile(StringUtil.REGEX_ALPHANUMERIC);
        // match regular expression to string and print matches
        Matcher matcher = expression.matcher(str);
        return matcher.find();
    }

    /**
     * Check if the string have any uppercase char
     *
     * @param str
     * @return
     */
    public static Boolean hasUpperCase(String str) {
        // create regular expression
        Pattern expression = Pattern.compile(StringUtil.REGEX_UPPERCASE);
        // match regular expression to string and print matches
        Matcher matcher = expression.matcher(str);
        return matcher.find();
    }

    /**
     * Check if the string have any lowercase char
     *
     * @param str
     * @return
     */
    public static Boolean hasLowerCase(String str) {
        // create regular expression
        Pattern expression = Pattern.compile(StringUtil.REGEX_LOWERCASE);
        // match regular expression to string and print matches
        Matcher matcher = expression.matcher(str);
        return matcher.find();
    }

    /**
     * Check if the String does not have Alphanumerics
     *
     * @param str
     * @return
     */
    public static Boolean notHasAlphanumeric(String str) {
        return !StringUtil.hasAlphanumeric(str);
    }

    /**
     * Generate random string with fixed size
     *
     * @return
     */
    public static String generateRandomString() {
        return generateRandomString(LENGTH_STRING_RANDOM);
    }

    /**
     * Generate a random string on the specified size
     *
     * @param length
     * @return
     */
    public static String generateRandomString(Integer length) {
        Random random = new Random();
        String retorno = "";
        for (int i = 0; i <= length; i++) {
            int r = random.nextInt(CARACTERES.length() - 1);
            retorno += CARACTERES.substring(r, r + 1);
        }
        return retorno;
    }

    /**
     * check if the strings are equals ignoring marks(acentos) and ignoreCase
     *
     * @param txt
     * @param txt2
     * @return
     */
    public static boolean equalsIgnoreCaseAcento(String txt, String txt2) {
        return getStringSemAcento(txt).trim().equalsIgnoreCase(getStringSemAcento(txt2).trim());
    }

    /**
     * remove pontuation from string
     *
     * @param txt
     * @return
     */
    public static String getStringSemAcento(String txt) {
        String s = "";
        for (int i = 0; i < txt.length(); i++) {
            char c = txt.charAt(i);
            switch (c) {
                case 'Á':
                case 'À':
                case 'Ã':
                    c = 'A';
                    break;
                case 'É':
                case 'Ê':
                    c = 'E';
                    break;
                case 'Í':
                    c = 'I';
                    break;
                case 'Ó':
                case 'Õ':
                case 'Ô':
                    c = 'O';
                    break;
                case 'Ú':
                    c = 'U';
                    break;
                case 'Ç':
                    c = 'C';

                case 'á':
                case 'à':
                case 'ã':
                    c = 'a';
                    break;
                case 'é':
                case 'ê':
                    c = 'e';
                    break;
                case 'í':
                    c = 'i';
                    break;
                case 'ó':
                case 'õ':
                case 'ô':
                    c = 'o';
                    break;
                case 'ú':
                    c = 'u';
                    break;
                case 'ç':
                    c = 'c';
                    break;
            }
            s += c;
        }
        return s;
    }


}
