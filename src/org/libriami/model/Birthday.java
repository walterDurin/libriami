/*
 * Copyright 2010 Dietrich Pfeifle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.libriami.model;

import java.text.DateFormat;
import java.util.Calendar;

public class Birthday {

	private Calendar bdaycal;

	public Birthday(int day, int month, int year) {
		bdaycal = Calendar.getInstance(Settings.TIMEZONE, Settings.LOCALE);
		bdaycal.set(Calendar.MONTH, month - 1);
		bdaycal.set(Calendar.DATE, day);
		bdaycal.set(Calendar.YEAR, year);
		bdaycal.set(Calendar.HOUR, 0);
		bdaycal.set(Calendar.MINUTE, 0);
		bdaycal.set(Calendar.SECOND, 0);
		bdaycal.set(Calendar.MILLISECOND, 0);
	}

	public Birthday(int day, int month) {
		bdaycal = Calendar.getInstance(Settings.TIMEZONE, Settings.LOCALE);
		bdaycal.set(Calendar.MONTH, month - 1);
		bdaycal.set(Calendar.DATE, day);
		bdaycal.set(Calendar.HOUR, 0);
		bdaycal.set(Calendar.MINUTE, 0);
		bdaycal.set(Calendar.SECOND, 0);
		bdaycal.set(Calendar.MILLISECOND, 0);
	}

	public Integer getYear() {
		return bdaycal.get(Calendar.YEAR);
	}

	public void setYear(Integer year) {
		bdaycal.set(Calendar.YEAR, year);
	}

	public Integer getMonth() {
		return bdaycal.get(Calendar.MONTH) + 1;
	}

	public void setMonth(Integer month) {
		bdaycal.set(Calendar.MONTH, month - 1);
	}

	public Integer getDay() {
		return bdaycal.get(Calendar.DATE);
	}

	public void setDay(Integer day) {
		bdaycal.set(Calendar.DATE, day);
	}

	public Calendar getCalendar() {
		return bdaycal;
	}

	public Calendar getNextBirthday() {
		Calendar now = Calendar.getInstance(Settings.TIMEZONE, Settings.LOCALE);
		// Create a copy cause we modify it
		Calendar bday = (Calendar) bdaycal.clone();
		if (bday.before(now)) {
			bday.set(Calendar.YEAR, now.get(Calendar.YEAR));
			if (bday.before(now)) {
				bday.add(Calendar.YEAR, 1);
			}
		}
		return bday;
	}

	public int getDaysTillNextBirthday() {
		Calendar now = Calendar.getInstance(Settings.TIMEZONE, Settings.LOCALE);
		Calendar bday = getNextBirthday();
		int daysTillBday = (int) ((bday.getTimeInMillis() - now.getTimeInMillis()) / (24 * 60 * 60 * 1000));
		return daysTillBday;
	}

	@Override
	public String toString() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		return df.format(bdaycal.getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bdaycal == null) ? 0 : bdaycal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Birthday other = (Birthday) obj;
		if (bdaycal == null) {
			if (other.bdaycal != null)
				return false;
		} else if (!bdaycal.equals(other.bdaycal))
			return false;
		return true;
	}

}
