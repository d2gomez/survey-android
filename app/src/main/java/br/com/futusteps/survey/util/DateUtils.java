package br.com.futusteps.survey.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    public static final String TIME_ZONE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static Calendar getCalendar(String format, String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            calendar.setTime(df.parse(date));
            return calendar;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String format(Calendar calendar, String format) {
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(calendar.getTime());
    }

    @SuppressWarnings("SameParameterValue")
    public static String changeFormat(String date, String oldFormat, String newFormat) {
        if (!StringUtils.isEmpty(date) && !StringUtils.isEmpty(oldFormat)) {
            Calendar cal = getCalendar(oldFormat, date);
            if (cal != null && !StringUtils.isEmpty(newFormat)) {
                return format(cal, newFormat);
            }
        }
        return "";
    }

    public static String changeDefaultFormat(String date) {
        return changeFormat(date, TIME_ZONE_DATE_FORMAT, "dd/MM/yyyy 'às' HH:mm");
    }
}
