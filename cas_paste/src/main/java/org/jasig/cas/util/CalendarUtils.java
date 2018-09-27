package org.jasig.cas.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author SxL
 * Created on 9/25/2018 5:09 PM.
 */
public final class CalendarUtils {
    public static final String[] WEEKDAYS = new String[]{"UNDEFINED", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    private CalendarUtils() {
    }

    public static int getCurrentDayOfWeek() {
        return getCurrentDayOfWeekFor(new Date());
    }

    public static int getCurrentDayOfWeekFor(Date date) {
        return getCalendarFor(date).get(7);
    }

    public static Calendar getCalendarFor(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}

