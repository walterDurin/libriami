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
import java.io.File;
import java.io.FileReader;
import java.util.List;

import junit.framework.TestCase;

import org.libriami.coder.DecoderFactory;
import org.libriami.model.AddressBook;
import org.libriami.model.Contact;
import org.libriami.model.Settings;
import org.libriami.model.tools.compare.FixDuplicates;
import org.libriami.model.tools.compare.StringCompare;

public class TestDuplicates extends TestCase {

	public void testStringCompare() {
		assertEquals(0d, StringCompare.compare("foo", "bar"));
		assertEquals(0d, StringCompare.compare("", "bar"));
		assertEquals(0d, StringCompare.compare("foo", ""));
		assertEquals(1d, StringCompare.compare("", ""));
		assertEquals(0d, StringCompare.compare("a", "b"));
		assertEquals(Settings.IGNORECASE_STRING_COMPARE ? 1d : 0d, StringCompare.compare("a", "A"));
		assertEquals(1d, StringCompare.compare("foo", "foo"));
		assertEquals(Settings.IGNORECASE_STRING_COMPARE ? 1d : 0.66d, StringCompare.compare("foo", "Foo"), 0.01);
		assertEquals(0.66, StringCompare.compare("foo", "xoo"), 0.01);
		assertEquals(0.93,
				StringCompare.compare("The quick brown fox jumps over the lazy dog", "The quick brown fox hops over the lazy dog"), 0.01);
		assertEquals(0.9, StringCompare.compare("Hans Test", "Hanss Test"), 0.01);
		assertEquals(0.9, StringCompare.compare("Hansi Test", "Hans Test"), 0.01);
	}

	public void testFixDuplicates() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader("tests/test_duplicates.ldif"));
		AddressBook addressBook = DecoderFactory.getDecoder(new File("tests/test_duplicates.ldif")).decode(in);
		in.close();

		// addressBook = FixDuplicateContacts.fix(addressBook);
		FixDuplicates<Contact> fixDuplicates = new FixDuplicates<Contact>();
		List<Contact> fixedContacts = fixDuplicates.fix(addressBook.getContacts());
		addressBook.setContacts(fixedContacts);
		assertEquals(2, addressBook.getContacts().size());

		System.out.println("Fixed contact list:");
		int i = 1;
		for (Contact c : addressBook.getContacts()) {
			System.out.println((i++) + ": " + c);
		}
	}

}
