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

package org.libriami.tests.model;

import java.util.Calendar;

import junit.framework.TestCase;

import org.libriami.model.Birthday;
import org.libriami.model.Settings;

public class TestBirthday extends TestCase {

	public void testFill() throws Exception {
		Birthday b = new Birthday(24, 6, 1971);
		assertEquals((Integer) 24, b.getDay());
		assertEquals((Integer) 6, b.getMonth());
		assertEquals((Integer) 1971, b.getYear());

		Calendar now1 = Calendar.getInstance(Settings.TIMEZONE, Settings.LOCALE);
		b = new Birthday(24, 6);
		assertEquals((Integer) 24, b.getDay());
		assertEquals((Integer) 6, b.getMonth());
		assertEquals((Integer) now1.get(Calendar.YEAR), b.getYear());
	}

	public void testSomeCalculations() throws Exception {
		Birthday b = new Birthday(24, 6, 1971);

		System.out.println("Nächster Geburtstag ist am " + b.getNextBirthday().getTime());

		System.out.println("Noch " + b.getDaysTillNextBirthday() + " Tage bis zum Geburtstag");
	}
}
