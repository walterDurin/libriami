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

package org.libriami;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.libriami.coder.Decoder;
import org.libriami.coder.DecoderFactory;
import org.libriami.coder.Encoder;
import org.libriami.coder.EncoderFactory;
import org.libriami.model.AddressBook;
import org.libriami.model.Contact;
import org.libriami.model.tools.compare.FixDuplicates;

/**
 * @author pfd2pl
 * 
 */
public class CmdLine {

	private final static String HELP = "h";

	private final static String FORMAT = "f";
	private static final String FORMAT_LDIF = "ldif";
	private static final String FORMAT_VCARD = "vcard";
	private static final String FORMAT_DEFAULT = "default";

	private static final String FIX = "d";

	private static Option optionHelp = new Option(HELP, "Print this help message");
	private static Option optionFormat = new Option(FORMAT, true,
			"Output format, by default a simple list is displayed. ldif,vcard,default");
	private static Option optionFix = new Option(FIX, "Autofix duplicates and merge contacts");
	private static Options commandLineOptions = new Options().addOption(optionHelp).addOption(optionFormat).addOption(optionFix);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		Encoder encoder = null;
		AddressBook addressBook = new AddressBook();

		CommandLineParser parser = new BasicParser();
		try {
			CommandLine commandLine = parser.parse(commandLineOptions, args);

			// HELP

			if (commandLine.getArgs().length == 0 || commandLine.hasOption(HELP)) {
				printHelp();
				return;
			}

			// Check FORMAT

			String format = commandLine.getOptionValue(FORMAT, FORMAT_DEFAULT);
			if (format.equals(FORMAT_LDIF)) {
				encoder = EncoderFactory.getLdifEncoder();
			} else if (format.equals(FORMAT_VCARD)) {
				encoder = EncoderFactory.getVcardEncoder();
			} else if (format.equals(FORMAT_DEFAULT)) {
				encoder = null;
			} else {
				System.err.println("ERROR: Format could be 'ldif' or 'vcard'");
				printHelp();
				return;
			}

			// Work on FILEs

			String[] arguments = commandLine.getArgs();
			for (String argument : arguments) {
				File f = new File(argument);

				// Decode the file and create a temporary address book
				Decoder decoder = DecoderFactory.getDecoder(f);
				BufferedReader in = getUTF8Reader(f);
				AddressBook addressBook2 = decoder.decode(in);
				in.close();

				// Add all contacts from this file to our address book
				addressBook.getContacts().addAll(addressBook2.getContacts());
			}
			Collections.sort(addressBook.getContacts());

			// Dump

			for (Contact c : addressBook.getContacts()) {
				System.out.println("> " + c);
			}

			// Auto FIX duplicates

			if (commandLine.hasOption(FIX)) {
				// addressBook = FixDuplicateContacts.fix(addressBook);
				List<Contact> contacts = addressBook.getContacts();
				FixDuplicates<Contact> fixDuplicates = new FixDuplicates<Contact>();
				List<Contact> fixedContacts = fixDuplicates.fix(contacts);
				addressBook.setContacts(fixedContacts);
			}

			// Encode all contacts to STDOUT using encoder

			if (encoder == null) {
				int n = 1;
				for (Contact c : addressBook.getContacts())
					System.out.println((n++) + "> " + c);
			} else {
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
				encoder.encode(addressBook, out);
				out.flush();
				out.close();
			}

		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar libriami.jar [OPTION]... [FILE]...", commandLineOptions);
	}

	private static BufferedReader getUTF8Reader(File f) throws UnsupportedEncodingException, FileNotFoundException {
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(f), "UTF-8");
		BufferedReader in = new BufferedReader(inputStreamReader);
		return in;
	}
}
