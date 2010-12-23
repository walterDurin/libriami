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

package org.libriami.formats.ldif;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.libriami.coder.CoderListener;
import org.libriami.coder.ConsoleCoderListener;
import org.libriami.model.Address;
import org.libriami.model.AddressBook;
import org.libriami.model.Birthday;
import org.libriami.model.Contact;
import org.libriami.model.EmailAddress;
import org.libriami.model.ModelException;
import org.libriami.model.PhoneNumber;
import org.libriami.model.PhoneNumber.Type;
import org.libriami.utils.PushBackLineReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Decoder implements org.libriami.coder.Decoder {

	final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Override
	public AddressBook decode(BufferedReader bin) throws IOException {
		return decode(bin, new ConsoleCoderListener());
	}

	@Override
	public AddressBook decode(BufferedReader bin, CoderListener listener) throws IOException {

		AddressBook addressBook = new AddressBook();
		PushBackLineReader in = new PushBackLineReader(bin, false);

		LdifFile lf = pullFile(in);
		// lf.dump();
		in.close();

		List<LdifElement> elements = lf.getElements();
		for (LdifElement e : elements) {

			if (e.find("objectclass", "person") != null || e.find("objectclass", "person") != null
					|| e.find("objectclass", "person") != null) {

				// String dn = e.get("dn").getValue();
				String givenname = e.getFirst("givenName") != null ? e.getFirst("givenName").getValue() : "";
				String surname = e.getFirst("sn") != null ? e.getFirst("sn").getValue() : "";
				Contact contact = new Contact(givenname, surname);

				LdifData byear = e.getFirst("birthyear");
				LdifData bmonth = e.getFirst("birthmonth");
				LdifData bday = e.getFirst("birthday");
				if (bday != null && bmonth != null) {
					int day = Integer.parseInt(bday.getValue());
					int month = Integer.parseInt(bmonth.getValue());
					if (byear != null) {
						int year = Integer.parseInt(byear.getValue());
						contact.setBirthday(new Birthday(day, month, year));
					} else {
						contact.setBirthday(new Birthday(day, month));
					}
				}

				// l: Stuttgart
				// postalcode: 71234,
				// streetaddress: Groﬂe Straﬂe 8
				if (e.getFirst("l") != null || e.getFirst("postalcode") != null || e.getFirst("streetaddress") != null) {
					// FIXME: This is just the german style!! Needs to be more
					// flexible!
					StringBuilder sb = new StringBuilder();
					sb.append(e.getFirst("streetaddress") != null ? e.getFirst("streetaddress").getValue() : "").append("\n");
					sb.append(e.getFirst("postalcode") != null ? e.getFirst("postalcode").getValue() : "").append(" ");
					sb.append(e.getFirst("l") != null ? e.getFirst("l").getValue() : "").append("\n");
					Address address = new Address(sb.toString());
					contact.getAddress().add(address);
				}

				// mozillaHomeStreet: Hofackerstr. 41 73660 Urbach
				if (e.getFirst("mozillaHomeStreet") != null) {
					List<String> lines = new ArrayList<String>();
					lines.add(e.getFirst("mozillaHomeStreet").getValue());
					Address address = new Address(lines);
					contact.getAddress().add(address);
				}

				try {
					// mail: alfred.maxxx@beoxxx.com
					if (e.getFirst("mail") != null) {
						EmailAddress email = new EmailAddress(e.getFirst("mail").getValue());
						contact.getEmail().add(email);
					}
				} catch (ModelException exc) {
					listener.warn(contact, "Skipping email address" + exc.getMessage());
				}

				try {
					// mozillaSecondEmail: amaxx@axxx.de
					if (e.getFirst("mozillaSecondEmail") != null) {
						EmailAddress email = new EmailAddress(e.getFirst("mozillaSecondEmail").getValue());
						contact.getEmail().add(email);
					}
				} catch (ModelException exc) {
					listener.warn(contact, "Skipping email address" + exc.getMessage());
				}

				// telephoneNumber: +4915xx // WORK
				if (e.getFirst("telephoneNumber") != null) {
					PhoneNumber number = new PhoneNumber(e.getFirst("telephoneNumber").getValue(), Type.WORK);
					contact.getPhone().add(number);
				}

				// mobile: +49151xx // MOBILE
				if (e.getFirst("mobile") != null) {
					PhoneNumber number = new PhoneNumber(e.getFirst("mobile").getValue(), PhoneNumber.Type.CELL);
					contact.getPhone().add(number);
				}

				// homePhone: +49718xxx // HOME
				if (e.getFirst("homePhone") != null) {
					PhoneNumber number = new PhoneNumber(e.getFirst("homePhone").getValue(), Type.HOME);
					contact.getPhone().add(number);
				}

				addressBook.add(contact);

			} else {

				// Skipping LDIF element because no contact information
				listener.warn(null, "Skipping LDIF element, because it does not contain person objectclass: " + e.getFirst("dn"));

			}
		}

		return addressBook;
	}

	public static LdifFile pullFile(PushBackLineReader in) throws IOException {
		LdifFile file = new LdifFile();
		LdifElement dataSet = null;
		while ((dataSet = pullElement(in)) != null) {
			file.add(dataSet);
		}
		return file;
	}

	public static LdifElement pullElement(PushBackLineReader in) throws IOException {
		LdifElement dataSet = new LdifElement();
		LdifData data = null;

		while ((data = pullLdifData(in)) != null) {
			dataSet.add(data);
		}

		if (dataSet.isEmpty())
			return null;
		else {
			return dataSet;
		}
	}

	public static LdifData pullLdifData(PushBackLineReader in) throws IOException {
		// Read 1st line
		String line = in.readLine();
		if (line == null)
			return null;

		if (line.trim().length() == 0)
			return null;

		if (line.indexOf(':') < 0)
			throw new IOException("Ldif data line requires a : character to separate key and value. Instead we got '" + line + "'");

		// LDIF has multi line support, so we club them together here

		boolean endReached = false;
		do {
			String nextLine = in.readLine();
			if (nextLine == null) {
				endReached = true;
			} else {
				boolean emptyLine = nextLine.length() <= 0;

				if ((!emptyLine && nextLine.charAt(0) == ' ') || (!emptyLine && !nextLine.contains(":"))) {
					// Next line belongs to value
					nextLine = nextLine.trim();
					line += nextLine;
				} else {
					in.pushBack(nextLine);
					endReached = true;
				}
			}
		} while (!endReached);

		return parseLine(line);
	}

	public static LdifData parseLine(String line) throws IOException {
		if (line.contains("::"))
			return parseBase64Line(line);
		else if (line.contains(":"))
			return parseAsciiLine(line);
		else
			throw new IOException("No : delimiter found");
	}

	private static LdifData parseAsciiLine(String line) {
		int idx = line.indexOf(':');
		String name = line.substring(0, idx).trim();
		String value = line.substring(idx + 1).trim();
		return new LdifData(name, value);
	}

	private static LdifData parseBase64Line(String line) {
		int idx = line.indexOf(':');
		String name = line.substring(0, idx).trim();
		String value = line.substring(idx + 2).trim();
		return new LdifData(name, value, true);
	}

}
