package Util;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.lang.StringBuilder;

public class Dates {

	static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
	static StringBuilder range;

	public static DateTime toDateTime(String date){

		return fmt.parseDateTime(date);
	}

	public static String toString(DateTime date)
	{
		return date.toString(fmt);
	}

	public static int toHour(String date){
		return ((fmt.parseDateTime(date)).getHourOfDay());
	}

	public static String toHourString(String date){
		int hour = ((fmt.parseDateTime(date)).getHourOfDay());
		return "hour"+hour;
	}

	public static int toDay(String date){
		return ((fmt.parseDateTime(date)).getDayOfWeek());
	}

	public static String toDayString(String date){
		int day = ((fmt.parseDateTime(date)).getDayOfWeek());
		String weekday = "";
		switch(day){
		case 1:
			weekday = "Monday";
			break;
		case 2: 
			weekday = "Tuesday";
			break;
		case 3: 
			weekday = "Wednesday";
			break;
		case 4: 
			weekday = "Thirsday";
			break;
		case 5: 
			weekday = "Friday";
			break;
		case 6: 
			weekday = "Saturday";
			break;
		case 7: 
			weekday = "Sunday";
			break;
		}
		return weekday;
	}

	public static int getMinuteDuration(String start, String end)
	{
		return Minutes.minutesBetween(toDateTime(start), toDateTime(end)).getMinutes();
	}

	public static int getMonthDuration(String start, String end)
	{
		return Months.monthsBetween(toDateTime(start), toDateTime(end)).getMonths();
	}

	public static String monthsLater(String start, int numMonths)
	{
		return toString(toDateTime(start).plusMonths(numMonths));
	}

	public static void main(String args[])
	{
		DateTime d = new DateTime("2000-05-04T01:02:03");
		System.out.print(toString(d));
	}

	public static String getRange(String rangeStart, String rangeEnd) {
		DateTime start = toDateTime(rangeStart);
		DateTime end = toDateTime(rangeEnd);
		range = new StringBuilder();
		range.append(start.getYear());
		range.append(".");
		range.append(start.getMonthOfYear());
		range.append(".");
		range.append(start.getDayOfMonth());
		range.append("~");
		range.append(end.getYear());
		range.append(".");
		range.append(end.getMonthOfYear());
		range.append(".");
		range.append(end.getDayOfMonth());
		return range.toString();
	}
}
