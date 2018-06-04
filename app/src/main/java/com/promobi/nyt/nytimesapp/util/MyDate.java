package com.promobi.nyt.nytimesapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dw on 23/02/17.
 */

public class MyDate {
    // Regex matching strings starting with a YYYY-MM-DD date (format of dates in API JSON response)
    private static final Pattern mApiJsonPattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}).*");
    private static final Pattern mApiHttpPattern = Pattern.compile("^\\d{8}$");

    // Format for parsing the YYYY-MM-DD date that was extracted with above pattern
    private static final SimpleDateFormat mApiJsonDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat mApiHttpDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);

    // Output date formats
    private static final SimpleDateFormat mOutFormat1 = new SimpleDateFormat("d MMM. yyyy", Locale.US);
    private static final SimpleDateFormat mOutFormat3 = new SimpleDateFormat("EEE. d MMM. yyyy", Locale.US);
    private static final SimpleDateFormat mOutFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat mOutFormat4 = new SimpleDateFormat("yyyyMMdd", Locale.US);


    private Date mDate;

    /**
     * Create a MyDate object by passing a string starting of one of the following formats:
     * - Starting with "YYYY-MM-DD" (date format of JSON response from API)
     * - "YYYYMMDD" (date format of HTTP requests for API)
     */

    public MyDate(String str) {
        Matcher matcher;
        matcher = mApiJsonPattern.matcher(str);
        if (matcher.find()) {
            try {
                mDate = mApiJsonDateFormat.parse(matcher.group(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            matcher = mApiHttpPattern.matcher(str);
            if (matcher.find()) {
                try {
                    mDate = mApiHttpDateFormat.parse(matcher.group());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Create a MyDate object by specifying year, month, and day.
     * @param year e.g. 2017
     * @param month 0-11
     * @param day 1-31
     */
    public MyDate(int year, int month, int day) {
        mDate = (new GregorianCalendar(year, month, day).getTime());
    }

    // Format the date with output format 1
    public String format1() {
        return format(mOutFormat1);
    }

    // Format the date with output format 1
    public String format2() {
        return format(mOutFormat2);
    }

    public String format3() {
        return format(mOutFormat3);
    }

    public String format4() {
        return format(mOutFormat4);
    }

    private String format(SimpleDateFormat f) {
        if (mDate != null)
            return f.format(mDate);
        else
            return null;
    }

    public Date getDate() {
        return mDate;
    }

    public int getYear() {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(mDate);
        return c.get(Calendar.YEAR);
    }

    public int getMonth() {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(mDate);
        return c.get(Calendar.MONTH);
    }

    public int getDay() {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(mDate);
        return c.get(Calendar.DAY_OF_MONTH);
    }



}
