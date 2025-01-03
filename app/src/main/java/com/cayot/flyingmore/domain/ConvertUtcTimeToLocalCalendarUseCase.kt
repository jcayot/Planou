package com.cayot.flyingmore.domain;

import java.util.Calendar
import java.util.TimeZone

class ConvertUtcTimeToLocalCalendarUseCase {
	/**
	 * Converts a given UTC time to the local time of the specified time zone.
	 *
	 * @param utcTime The UTC time in milliseconds.
	 * @param timeZone The time zone string (e.g., "America/New_York").
	 * @return A Calendar instance set to the local time.
	 */
	operator fun invoke(utcTime: Long, timeZone: String): Calendar {
		return (Calendar.getInstance(TimeZone.getTimeZone(timeZone)).apply {
			timeInMillis = utcTime
		})
	}
}
