package com.school.eventrra.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String hourMinuteAmPm(Date d) {
        return new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(d);
    }

    public static String stdDateFormat(Date d) {
        return new SimpleDateFormat("EEE dd, MMM yyyy", Locale.ENGLISH).format(d);
    }

    public static long getFirstDayOfCurrentWeek() {
        Calendar cal = getTodayWithoutTime();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        return cal.getTime().getTime();
    }

    public static long getFirstDayOfNextWeek() {
        Calendar cal = getTodayWithoutTime();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        return cal.getTime().getTime();
    }

    private static Calendar getTodayWithoutTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }
}
