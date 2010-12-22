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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import junit.framework.TestCase;

import org.libriami.coder.DecoderFactory;
import org.libriami.coder.EncoderFactory;
import org.libriami.model.AddressBook;
import org.libriami.model.Contact;
import org.libriami.tests.TestFiles;

public class TestLdifToVcard extends TestCase {

	public void testLdifContactsFromFile() throws Exception {

		BufferedReader in = new BufferedReader(new FileReader(TestFiles.BIG_EXAMPLE));
		AddressBook addressBook = DecoderFactory.getLdifDecoder().decode(in);
		in.close();

		List<Contact> contacts = addressBook.getContacts();

		System.out.println(contacts.size() + " contacts found.");

		File f = File.createTempFile("libriami", "junit.vcf");
		BufferedWriter out = new BufferedWriter(new FileWriter(f));
		EncoderFactory.getDecoder(f).encode(addressBook, out);
		out.flush();
		out.close();
	}
}
