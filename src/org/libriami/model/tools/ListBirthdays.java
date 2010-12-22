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

package org.libriami.model.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import org.libriami.coder.ConsoleCoderListener;
import org.libriami.coder.Decoder;
import org.libriami.coder.DecoderFactory;
import org.libriami.model.AddressBook;
import org.libriami.model.Birthday;
import org.libriami.model.Contact;

public class ListBirthdays {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("ERROR: Specify one filename as argument to" + " load contacts from. Either a LDIF or VCARD file.");
			return;
		}

		AddressBook addressBook;

		File f = new File(args[0]);
		Decoder decoder = DecoderFactory.getDecoder(f);
		BufferedReader in = DecoderFactory.getUTF8Reader(f);
		addressBook = decoder.decode(in, new ConsoleCoderListener());
		in.close();

		// Sort them

		Collections.sort(addressBook.getContacts(), new Comparator<Contact>() {
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

		// Now dump the contacts to STDOUT

		for (Contact c : addressBook.getContacts()) {
			String s = c.toString();
			if (c.getBirthday() != null)
				System.out.println(s);
		}
	}

}
