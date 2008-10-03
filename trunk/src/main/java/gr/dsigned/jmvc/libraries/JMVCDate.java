package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.framework.Library;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class JMVCDate extends Library {

    public static final long seconds = 1000;
    public static final long minutes = 60 * seconds;
    public static final long hours = 60 * minutes;
    public static final long days = 24 * hours;
    public static final long weeks = 24 * days;
    public static final long months = 30 * days;
    public static final long years = 12 * months;

    public static enum TIMESPAN {

        YEAR, MONTH, WEEK, DAY, HOUR, MIN, SEC
    }

    /**
     * Produces a human readable timespan description.
     * i.e.: 1 year 2 months 1 week 2 days 2 hours 21 minutes
     * @param startDate The starting date of the time span.
     * @param endDate The ending date of the time span.
     * @param lowestUnit the lowest time unit we need to display. i.e if you choose DAY the timespan will not display hours or seconds
     * @return
     */
    public static String timespan(Date startDate, Date endDate, TIMESPAN lowestUnit) {
        StringBuilder sb = new StringBuilder();

        long timestamp = endDate.getTime() - startDate.getTime();

        int yer = getFlooredValue(timestamp, years);
        timestamp -= yer * years;

        int mon = getFlooredValue(timestamp, months);
        timestamp -= mon * months;

        int wek = getFlooredValue(timestamp, weeks);
        timestamp -= wek * weeks;

        int day = getFlooredValue(timestamp, days);
        timestamp -= day * days;

        int hor = getFlooredValue(timestamp, hours);
        timestamp -= hor * hours;

        int min = getFlooredValue(timestamp, minutes);
        timestamp -= min * minutes;

        int sec = getFlooredValue(timestamp, seconds);
        switch (lowestUnit) {
            case SEC:
                if (sec != 0) {
                    sb.insert(0, sec + ((sec > 1) ? " δευτερόλεπτα " : " δευτερόλεπτο "));
                }
            case MIN:
                if (min != 0) {
                    sb.insert(0, min + ((min > 1) ? " λεπτά " : " λεπτό "));
                }
            case HOUR:
                if (hor != 0) {
                    sb.insert(0, hor + ((hor > 1) ? " ώρες " : " ώρα "));
                }
            case DAY:
                if (day != 0) {
                    sb.insert(0, day + ((day > 1) ? " μέρες " : " μέρα "));
                }

            case WEEK:
                if (wek != 0) {
                    sb.insert(0, wek + ((wek > 1) ? " εβδομάδες " : " εβδομάδα "));
                }
            case MONTH:
                if (mon != 0) {
                    sb.insert(0, mon + ((mon > 1) ? " μήνες " : " μήνας "));
                }
            case YEAR:
                if (yer != 0) {
                    sb.insert(0, yer + ((yer > 1) ? " χρόνια " : " χρόνος "));
                }
        }
        return sb.toString();
    }

    public static String timestampToDate(String timestamp) {
        long t = Long.valueOf(timestamp);
        Date d = new Date(t);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    public static String formatDatetime(String datetime, String pattern) {
        String output = "";
        try {
            SimpleDateFormat inFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            Date d = inFormatter.parse(datetime);
            SimpleDateFormat outFormatter = new SimpleDateFormat(pattern);
            output = outFormatter.format(d);
        } catch (ParseException ex) {
        }
        return output;
    }

    private static int getFlooredValue(Long v, Long d) {
        return (int) Math.floor(v / d);
    }
}
