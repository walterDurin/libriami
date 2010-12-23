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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.libriami.coder.CoderListener;
import org.libriami.coder.ConsoleCoderListener;
import org.libriami.model.Address;
import org.libriami.model.AddressBook;
import org.libriami.model.Birthday;
import org.libriami.model.Contact;
import org.libriami.model.PhoneNumber;
import org.libriami.utils.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encoder implements org.libriami.coder.Encoder {

	final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Override
	public void encode(AddressBook addressBook, BufferedWriter out) throws IOException {
		encode(addressBook, out, new ConsoleCoderListener());
	}

	@Override
	public void encode(AddressBook addressBook, BufferedWriter out, CoderListener listener) throws IOException {

		List<Contact> contacts = addressBook.getContacts();
		for (Contact c : contacts) {
			LdifElement le = new LdifElement();
			fillLdifElement(le, c);
			writeLdifElement(le, out);
			out.newLine();
		}
		out.flush();
	}

	public static void writeLdifElement(LdifElement le, BufferedWriter out) throws IOException {
		for (LdifData data : le.getData()) {
			writeLdifData(data, out);
			out.newLine();
		}
	}

	public static void writeLdifData(LdifData data, BufferedWriter out) throws IOException {
		StringBuilder sb = new StringBuilder();
		String valueAsString = data.getValue();
		if (valueAsString == null) {
			throw new IOException("Value of name " + data.getName() + " is null ");
		}

		boolean needsBase64Encoding = false;
		for (int i = 0; i < valueAsString.length(); i++) {
			char ch = valueAsString.charAt(i);
			if (ch < 32 || ch >= 127) {
				needsBase64Encoding = true;
				break;
			}
		}

		if (needsBase64Encoding) {
			try {
				valueAsString = org.apache.commons.codec.binary.Base64.encodeBase64String(valueAsString.getBytes("UTF-8"));
				// Trim is required cause we get a CRLF
				valueAsString = valueAsString.trim();
				StringEncoder.encode(data.getName() + ":: " + valueAsString, sb);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		} else {
			StringEncoder.encode(data.getName() + ": " + valueAsString, sb);
		}
		out.write(sb.toString());
	}

	private static void fillLdifElement(LdifElement le, Contact c) {
		String givenname = c.getGivenname();
		String surname = c.getSurname();
		String commonname = "cn=" + givenname + " " + surname;
		String email = null;
		String dn = commonname;
		if (c.getEmail().size() > 0) {
			email = c.getEmail().get(0).getEmail();
			dn += ", mail=" + email;
		}

		// System.out.println(">> " + dn);

		le.add(new LdifData("dn", dn));
		addObjectClasses(le);
		le.add(new LdifData("givenName", givenname));
		le.add(new LdifData("sn", surname));
		le.add(new LdifData("cn", commonname));
		if (email != null) {
			le.add(new LdifData("mail", email));
			if (c.getEmail().size() > 1) {
				le.add(new LdifData("mozillaSecondEmail", c.getEmail().get(1).getEmail()));
			}
			if (c.getEmail().size() > 2) {
				warn("Not more than 2 mail addresses supported: dn: " + dn);
			}
		}

		if (c.getAddress().size() > 0) {
			// mozillaHomeStreet: Kirchgasse 11 73660 Urbach
			Address address = c.getAddress().get(0);
			StringBuilder sb = new StringBuilder();
			List<String> lines = address.getLines();
			for (int i = 0; i < lines.size(); i++) {
				sb.append(lines.get(i));
				if ((i + 1) < lines.size())
					sb.append(", ");
			}
			// FIXME: Do not add all into home street only. Use specific fields
			le.add(new LdifData("mozillaHomeStreet", sb.toString()));
		}

		if (c.getPhone().size() > 0) {
			String keys[] = { "telephoneNumber", "homePhone", "mobile" };

			for (int i = 0; i < c.getPhone().size() && i < keys.length; i++) {
				PhoneNumber phoneNumber = c.getPhone().get(i);
				switch (phoneNumber.getType()) {
				case CELL:
					le.add(new LdifData("mobile", c.getPhone().get(i).getNumber()));
					break;
				case HOME:
					le.add(new LdifData("homePhone", c.getPhone().get(i).getNumber()));
					break;
				case WORK:
					le.add(new LdifData("telephoneNumber", c.getPhone().get(i).getNumber()));
					break;
				default:
					le.add(new LdifData("homePhone", c.getPhone().get(i).getNumber()));
				}
			}
		}

		if (c.getBirthday() != null) {
			Birthday birthday = c.getBirthday();
			le.add(new LdifData("birthyear", birthday.getYear().toString()));
			le.add(new LdifData("birthmonth", birthday.getMonth().toString()));
			le.add(new LdifData("birthday", birthday.getDay().toString()));
		}

	}

	private static void addObjectClasses(LdifElement le) {
		le.add(new LdifData("objectclass", "top"));
		le.add(new LdifData("objectclass", "person"));
		le.add(new LdifData("objectclass", "organizationalPerson"));
		le.add(new LdifData("objectclass", "inetOrgPerson"));
		le.add(new LdifData("objectclass", "mozillaAbPersonAlpha"));
	}

	private static void warn(String s) {
		System.err.println(s);
	}

	public static void encodeFile(LdifFile lf, BufferedWriter out) throws IOException {
		for (LdifElement e : lf.getElements()) {
			Encoder.writeLdifElement(e, out);
		}
	}

}
