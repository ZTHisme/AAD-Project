package com.school.eventrra.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String hourMinuteAmPm(Date d) {
        return new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(d);
    }

    public static String stdDateFormat(Date d) {
        return new SimpleDateFormat("EEE dd, MMM", Locale.ENGLISH).format(d);
    }
}
