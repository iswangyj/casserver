package org.jasig.cas.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author SxL
 * Created on 9/25/2018 5:14 PM.
 */
public final class SamlDateUtils {
    private SamlDateUtils() {
    }

    public static String getCurrentDateAndTime() {
        return getFormattedDateAndTime(new Date());
    }

    public static String getFormattedDateAndTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return dateFormat.format(date);
    }
}