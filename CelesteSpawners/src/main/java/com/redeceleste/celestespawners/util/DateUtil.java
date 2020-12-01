package com.redeceleste.celestespawners.util;

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
            if (days == 1) sb.append(" dia");
            else sb.append(" dias");
        }

        else if (hours > 0) {
            sb.append(hours);
            if (hours == 1) sb.append(" hora");
            else sb.append(" horas");
        }

        else if (minutes > 0) {
            sb.append(minutes);
            if (minutes == 1) sb.append(" minuto");
            else sb.append(" minutos");
        }

        else if (seconds > 0) {
            sb.append(seconds);
            if (seconds == 1) sb.append(" segundo");
            else sb.append(" segundos");
        }

        if (sb.toString().isEmpty()) return "0 segundos";
        return sb.toString();
    }
}
