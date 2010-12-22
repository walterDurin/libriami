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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.libriami.coder.ConsoleCoderListener;
import org.libriami.coder.Decoder;
import org.libriami.coder.DecoderFactory;
import org.libriami.model.AddressBook;
import org.libriami.model.Contact;
import org.libriami.model.tools.compare.FixDuplicates;

public class ListContacts {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("ERROR: Specify one filename as argument to" + " load contacts from. Either a LDIF or VCARD file.");
			return;
		}

		AddressBook addressBook;

		File f = new File(args[0]);
		Decoder decoder = DecoderFactory.getDecoder(f);
		BufferedReader in = getUTF8Reader(f);
		addressBook = decoder.decode(in, new ConsoleCoderListener());
		in.close();

		// addressBook = FixDuplicateContacts.fix(addressBook);
		FixDuplicates<Contact> fixDuplicates = new FixDuplicates<Contact>();
		List<Contact> fixedContacts = fixDuplicates.fix(addressBook.getContacts());
		addressBook.setContacts(fixedContacts);

		// Now dump the contacts to STDOUT
		for (Contact c : addressBook.getContacts()) {
			System.out.println(c);
		}
		System.out.flush();
	}

	/**
	 * Creates a UTF-8 buffered input reader from a file.
	 */
	private static BufferedReader getUTF8Reader(File f) throws UnsupportedEncodingException, FileNotFoundException {
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(f), "UTF-8");
		BufferedReader in = new BufferedReader(inputStreamReader);
		return in;
	}

	// // XXX test with example5.ldif
	// private static BufferedWriter getStdOutWriter()
	// throws IOException {
	// // OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
	// // System.out, "UTF-8");
	// // return new BufferedWriter(outputStreamWriter);
	//
	// return new BufferedWriter(new FileWriter("test.ldif"));
	// }
}
