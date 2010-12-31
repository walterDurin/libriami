/*
 * Copyright libriami / 2010 Dietrich Pfeifle
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

package org.libriami.examples;

import java.io.BufferedReader;
import java.io.File;

import org.libriami.coder.ConsoleCoderListener;
import org.libriami.coder.DecoderFactory;
import org.libriami.model.AddressBook;
import org.libriami.model.Birthday;
import org.libriami.model.Contact;

public class PrintNextBirthday {

	public static void main(String[] args) throws Exception {
		File f = new File("tests/bigexample.ldif");
		BufferedReader in = DecoderFactory.getUTF8Reader(f);
		AddressBook addressBook = DecoderFactory.getLdifDecoder().decode(in, new ConsoleCoderListener());
		in.close();

		Contact c1 = null;
		for (Contact c : addressBook.getContacts()) {
			Birthday b2 = c.getBirthday();
			if (b2 != null) {
				if (c1 == null || c1.getBirthday() == null || b2.getCalendar().before(c1.getBirthday().getCalendar())) {
					c1 = c;
				}
			}
		}

		if (c1 != null) {
			System.out.println("Next birthday: " + c1.getGivenname() + " is next one in " + c1.getBirthday().getDaysTillNextBirthday()
					+ " days");
		} else {
			System.out.println("No birthdays found in your addressbook");
		}
	}

}
