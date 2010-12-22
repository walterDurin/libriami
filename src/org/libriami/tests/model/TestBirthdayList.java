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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;

import org.libriami.coder.DecoderFactory;
import org.libriami.model.AddressBook;
import org.libriami.model.Birthday;
import org.libriami.model.Contact;
import org.libriami.tests.TestFiles;

public class TestBirthdayList extends TestCase {

	public void testBirthdayList() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(TestFiles.BIG_EXAMPLE));

		AddressBook addressBook = DecoderFactory.getDecoder(TestFiles.BIG_EXAMPLE).decode(in);

		in.close();

		List<Contact> contacts = addressBook.getContacts();

		System.out.println(contacts.size() + " contacts found.");

		Collections.sort(contacts, new Comparator<Contact>() {
			public int compare(Contact o1, Contact o2) {
				Birthday b1 = o1.getBirthday();
				Birthday b2 = o2.getBirthday();
				if (b1 == null)
					return -1;
				if (b2 == null)
					return 1;

				Calendar bc1 = b1.getNextBirthday();
				Calendar bc2 = b2.getNextBirthday();
				return bc1.compareTo(bc2);
			}
		});

		for (Contact c : contacts) {
			if (c.getBirthday() != null)
				System.out.println(c.getBirthday().getDaysTillNextBirthday() + " " + c);
		}
	}
}
