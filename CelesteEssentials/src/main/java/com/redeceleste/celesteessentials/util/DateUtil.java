package com.redeceleste.celesteessentials.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class DateUtil {
    public static String formatDate(Long time) {
        time = System.currentTimeMillis() - time;

        long days = MILLISECONDS.toDays(time);
        long hours = MILLISECONDS.toHours(time)      - MILLISECONDS.toDays(time)    * 24;
        long minutes = MILLISECONDS.toMinutes(time)  - MILLISECONDS.toHours(time)   * 60;
        long seconds = MILLISECONDS.toSeconds(time)  - MILLISECONDS.toMinutes(time) * 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days);
            if (days == 1) sb.append(" day");
            else sb.append(" days");
        }

        if (hours > 0) {
            if (days > 0) {
                if (minutes > 0 || seconds > 0) sb.append(", ");
                else sb.append(" and ");
            }
            sb.append(hours);
            if (hours == 1) sb.append(" hour");
            else sb.append(" hours");
        }

        if (minutes > 0) {
            if (days > 0 || hours > 0) {
                if (seconds > 0) sb.append(", ");
                else sb.append(" and ");
            }
            sb.append(minutes);
            if (minutes == 1) sb.append(" minute");
            else sb.append(" minutes");
        }

        if (seconds > 0) {
            if (days > 0 || hours > 0 || minutes > 0) sb.append(" and ");
            else if (sb.length() > 0) sb.append(", ");
            sb.append(seconds);
            if (seconds == 1) sb.append(" second");
            else sb.append(" seconds");
        }
        return sb.toString();
    }
}
