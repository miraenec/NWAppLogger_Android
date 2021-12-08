package com.nextweb.nwapplogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

class CalendarUtil {
	static int currentHour() {
		Calendar oCalendar = Calendar.getInstance();
		int currMinute = oCalendar.get(Calendar.HOUR_OF_DAY);

		return currMinute;
	}

	static String currentDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		return dateFormat.format(calendar.getTime());
	}

	static String currentTime() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return dateFormat.format(calendar.getTime());
	}

	static String currentTime2() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

		return dateFormat.format(calendar.getTime());
	}

	static String currentTimeForFileName(int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, second);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmm");

		return dateFormat.format(calendar.getTime());
	}

	static String currentTime(int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, second);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return dateFormat.format(calendar.getTime());
	}

	static String getTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (time == 0)
			return "";
		else
			return dateFormat.format(calendar.getTime());
	}

	static String currentTimeForFileName(long time, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.add(Calendar.SECOND, second);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmm");

		return dateFormat.format(calendar.getTime());
	}

	static String currentTime(long time, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.add(Calendar.SECOND, second);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return dateFormat.format(calendar.getTime());
	}

	static int currentMinute() {
		Calendar oCalendar = Calendar.getInstance();
		int currMinute = oCalendar.get(Calendar.MINUTE);

		return currMinute;
	}

	static int currentSecond() {
		Calendar oCalendar = Calendar.getInstance();
		int currSecond = oCalendar.get(Calendar.SECOND);

		return currSecond;
	}

	static int currentMilliSecond() {
		Calendar oCalendar = Calendar.getInstance();
		int currMilliSecond = oCalendar.get(Calendar.MILLISECOND);

		return currMilliSecond;
	}

	public static void main(String[] args) {
		System.out.println(currentTime2());
	}
}
