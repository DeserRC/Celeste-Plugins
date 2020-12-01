package com.redeceleste.celestekits.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class DateUtil {
    public static String formatDate(long time) {
        time -= System.currentTimeMillis();

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

        else if (hours > 0) {
            sb.append(hours);
            if (hours == 1) sb.append(" hour");
            else sb.append(" hours");
        }

        else if (minutes > 0) {
            sb.append(minutes);
            if (minutes == 1) sb.append(" minute");
            else sb.append(" minutes");
        }

        else if (seconds > 0) {
            sb.append(seconds);
            if (seconds == 1) sb.append(" second");
            else sb.append(" seconds");
        }

        if (sb.toString().isEmpty()) return "0 seconds";
        return sb.toString();
    }
}
