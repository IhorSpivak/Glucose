package com.ixbiopharma.glucose.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 * <p>
 * Created by ivan on 10.04.17.
 */

public class TimeUtils {

    private static DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private static DateFormat dateNewsFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static String dateToDateFormat(Date date) {
        return dateFormat.format(date);
    }

    public static String dateToNewsDateFormat(Date date) {
        return dateNewsFormat.format(date);
    }

    public static String dateToTimeFormat(Date date) {
        return timeFormat.format(date);
    }

    public static String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
    }

    public static float hoursDifference(Date date1, Date date2) {
        final float MILLI_TO_HOUR = 1000 * 60 * 60;
        return (float) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
    }

    public static Date apiStringToDate(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        Date dateDate = new Date();

        try {
            dateDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateDate;
    }

    public static Date apiStringToDate2(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date dateDate = format.parse(date);
        return dateDate;
    }

    public static Date getPrevYearDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    public static String millsToApiString(long mills) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        Date dateDate = new Date(mills);
        return format.format(dateDate);
    }
}
