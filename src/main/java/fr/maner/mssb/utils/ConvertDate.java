package fr.maner.mssb.utils;

public class ConvertDate {

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public static String millisToShortDHMS(long duration) {
        duration /= ONE_SECOND;
        int seconds = (int) (duration % SECONDS);
        duration /= SECONDS;
        int minutes = (int) (duration % MINUTES);

        if (minutes == 0) {
            return String.format("%02ds", seconds);
        } else {
            return String.format("%01dm et %02ds", minutes, seconds);
        }
    }

}
