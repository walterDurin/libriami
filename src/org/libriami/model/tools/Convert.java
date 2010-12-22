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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.libriami.coder.ConsoleCoderListener;
import org.libriami.coder.Decoder;
import org.libriami.coder.DecoderFactory;
import org.libriami.coder.Encoder;
import org.libriami.coder.EncoderFactory;
import org.libriami.model.AddressBook;

public class Convert {

	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.err.println("ERROR: Specify command and one filename as argument to"
					+ " load contacts from. Either a LDIF or VCARD file.");
			System.err.println("-ldif Converts to LDIF format");
			System.err.println("-vcard Converts to VCARD format");
			return;
		}

		String command = args[0];
		String filename = args[1];

		AddressBook addressBook;

		File f = new File(filename);
		Decoder decoder = DecoderFactory.getDecoder(f);
		BufferedReader in = getUTF8Reader(f);
		addressBook = decoder.decode(in, new ConsoleCoderListener());
		in.close();

		// Now dump the contacts to STDOUT
		Encoder encoder = null;
		if (command.equals("-ldif"))
			encoder = EncoderFactory.getLdifEncoder();
		else if (command.equals("-vcard"))
			encoder = EncoderFactory.getVcardEncoder();
		else {
			System.err.println("ERROR: Unknown command '" + command + "'");
			return;
		}

		encoder.encode(addressBook, getStdOutWriter());
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

	// XXX test with example5.ldif
	private static BufferedWriter getStdOutWriter() throws UnsupportedEncodingException {
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out, "UTF-8");
		// OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
		// System.out);
		return new BufferedWriter(outputStreamWriter);
	}
}
