package com.renan.todo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utilities class for date operations
 *
 * @author renan
 */
public class DateUtil {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DATE_HOUR_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm ");
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");

    /**
     * get the difference, in days, from the date informed and current date
     *
     * @param startDate - startDate
     * @return - the difference, in days, from the date informed and current date
     */
    public static Long getDays(Date startDate) {
        return DateUtil.getDays(startDate, new Date());
    }

    /**
     * get the difference, in days, from the dates informed
     *
     * @param startDate - startDate
     * @param endDate   - endDate
     * @return - the difference, in days, from the dates informed
     */
    public static Long getDays(Date startDate, Date endDate) {
        long diffInMilliseconds = startDate.getTime() - endDate.getTime();
        long diffInSeconds = diffInMilliseconds / 1000;
        long diffInMinutes = diffInSeconds / 60;
        long diffInHours = diffInMinutes / 60;
        return diffInHours / 24;
    }

    /**
     * get the difference, in hours, from the date informed and current date
     *
     * @param startDate - startDate
     * @return - the difference, in hours, from the date informed and current date
     */
    public static Long getHours(Date startDate) {
        return DateUtil.getHours(startDate, new Date());
    }

    /**
     * get the difference, in hours, from the dates informed
     *
     * @param startDate - startDate
     * @param endDate   - endDate
     * @return - the difference, in hours, from the dates informed
     */
    public static Long getHours(Date startDate, Date endDate) {
        long diffInMilliseconds = startDate.getTime() - endDate.getTime();
        long diffInSeconds = diffInMilliseconds / 1000;
        long diffInMinutes = diffInSeconds / 60;
        return diffInMinutes / 60;
    }

    /**
     * get the difference, in minutes, from the date informed and current date
     *
     * @param startDate - startDate
     * @return - the difference, in minutes, from the date informed and current date
     */
    public static Long getMinutes(Date startDate) {
        return DateUtil.getMinutes(startDate, new Date());
    }

    /**
     * get the difference, in minutes, from the dates informed
     *
     * @param startDate - startDate
     * @param endDate   - endDate
     * @return - the difference, in minutes, from the dates informed
     */
    public static Long getMinutes(Date startDate, Date endDate) {
        long diffInMilliseconds = startDate.getTime() - endDate.getTime();
        long diffInSeconds = diffInMilliseconds / 1000;
        return diffInSeconds / 60;
    }

    /**
     * get the difference, in seconds, from the date informed and current date
     *
     * @param startDate - startDate
     * @return - the difference, in seconds, from the date informed and current date
     */
    public static Long getSeconds(Date startDate) {
        return DateUtil.getSeconds(startDate, new Date());
    }

    /**
     * get the difference, in seconds, from the dates informed
     *
     * @param startDate - startDate
     * @param endDate   - endDate
     * @return - the difference, in seconds, from the dates informed
     */
    public static Long getSeconds(Date startDate, Date endDate) {
        long diffInMilliseconds = startDate.getTime() - endDate.getTime();
        return diffInMilliseconds / 1000;
    }

    /**
     * Get the year from the date
     *
     * @param date - date
     * @return - the year from the date
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Get the month from the date
     *
     * @param date - date
     * @return - the month from the date
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Get a Date object from year and month and day
     *
     * @param year  - year
     * @param month - month
     * @param day   - day
     * @return - a Date object from year and month and day
     */
    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    /**
     * Get a Date object from year and month, day 1
     *
     * @param year  - year
     * @param month - month
     * @return - a Date object from year and month, day 1
     */
    public static Date getDate(int year, int month) {
        return DateUtil.getDate(year, month, 1);
    }
}

