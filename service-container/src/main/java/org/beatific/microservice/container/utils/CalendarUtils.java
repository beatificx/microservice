package org.beatific.microservice.container.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class CalendarUtils {
	
	public static Calendar addHours(Calendar cal, int amount) {
		Date date = new Date(cal.getTimeInMillis());
		
		Calendar newCal = Calendar.getInstance();
		newCal.setTime(DateUtils.addHours(date, amount));
		return newCal;
	}
}
