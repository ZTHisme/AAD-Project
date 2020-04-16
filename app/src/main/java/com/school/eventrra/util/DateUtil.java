package com.school.eventrra.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String hourMinuteAmPm(Date d) {
        return new SimpleDateFormat("hh:MM a", Locale.ENGLISH).format(d);
    }
}
