package fr.maner.mssb.utils;

public class ConvertDate {

	public final static long ONE_SECOND = 1000;
	public final static long SECONDS = 60;

	public final static long ONE_MINUTE = ONE_SECOND * 60;
	public final static long MINUTES = 60;

	public final static long ONE_HOUR = ONE_MINUTE * 60;
	public final static long HOURS = 24;

	public static String millisToShortDHMS(long duration) {
		String res = "";
		duration /= ONE_SECOND;
		int seconds = (int) (duration % SECONDS);
		duration /= SECONDS;
		int minutes = (int) (duration % MINUTES);
		duration /= MINUTES;
		int hours = (int) (duration % HOURS);
		
		if (hours == 0)
			res = String.format("%02dm et %02ds", minutes, seconds);
		else
			res = String.format("%02dh %02dm et %02ds", hours, minutes, seconds);

		return res;
	}

}
