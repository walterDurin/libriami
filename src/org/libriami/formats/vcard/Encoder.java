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

package org.libriami.formats.vcard;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.libriami.coder.CoderListener;
import org.libriami.coder.ConsoleCoderListener;
import org.libriami.model.Address;
import org.libriami.model.AddressBook;
import org.libriami.model.Birthday;
import org.libriami.model.Contact;
import org.libriami.model.EmailAddress;
import org.libriami.model.PhoneNumber;

public class Encoder implements org.libriami.coder.Encoder {

	public void encode(AddressBook addressBook, BufferedWriter out) throws IOException {
		encode(addressBook, out, new ConsoleCoderListener());
	}

	public void encode(AddressBook addressBook, BufferedWriter out, CoderListener listener) throws IOException {
		List<Contact> contacts = addressBook.getContacts();
		for (Contact contact : contacts) {
			VcardElement ve = new VcardElement();
			fillVcardElement(ve, contact);
			writeVcard(ve, out);
		}
	}

	private static void fillVcardElement(VcardElement vc, Contact contact) {
		vc.add("BEGIN", "VCARD");
		vc.add("VERSION", "2.1");
		// Family Name, Given Name, Additional Names, Honorific Prefixes,
		// and Honorific Suffixes
		vc.add(new VcardData("N", new String[] {}, new String[] { contact.getSurname(), contact.getGivenname(), "", "", "" }));

		vc.add(new VcardData("FN", contact.getGivenname() + " " + contact.getSurname()));
		Birthday bday = contact.getBirthday();
		if (bday != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(bday.getYear().toString());
			sb.append("-");
			sb.append(bday.getMonth().toString());
			sb.append("-");
			sb.append(bday.getDay().toString());
			vc.add(new VcardData("BDAY", sb.toString()));
		}

		if (contact.getNickname() != null)
			vc.add(new VcardData("NICKNAME", contact.getNickname()));

		for (Address a : contact.getAddress()) {
			String type = "HOME";
			if (a.getType().equals(Address.Type.WORK)) // FIXME
				type = "WORK";
			String p[] = a.getLines().toArray(new String[a.getLines().size()]);
			if (p.length > 0)
				vc.add(new VcardData("ADR", new String[] { type }, p)); // FIXME
		}

		for (PhoneNumber p : contact.getPhone()) {
			String type = "HOME";
			if (p.getType().equals(PhoneNumber.Type.DEFAULT)) // XXX
				type = "HOME";
			else if (p.getType().equals(PhoneNumber.Type.CELL)) // XXX
				type = "CELL";
			else if (p.getType().equals(PhoneNumber.Type.HOME))
				type = "HOME";
			else if (p.getType().equals(PhoneNumber.Type.WORK))
				type = "WORK";

			String a[] = new String[] { p.getNumber() };
			vc.add(new VcardData("TEL", new String[] { type }, a));
		}

		for (EmailAddress e : contact.getEmail()) {
			// FIXME: TYPE
			String type = "HOME";
			String a[] = new String[] { e.getEmail() };
			vc.add(new VcardData("EMAIL", new String[] { type }, a));
		}

		if (contact.getUrl() != null) {
			vc.add(new VcardData("URL", new String[] { "WORK" }, new String[] { contact.getUrl() }));
		}

		vc.add("END", "VCARD");
	}

	private static void writeVcard(VcardElement vc, BufferedWriter out) throws IOException {
		for (VcardData d : vc.getData()) {
			out.write(d.getName());
			for (int i = 0; i < d.getParameter().size(); i++) {
				out.write(";");
				out.write(d.getParameter().get(i));
			}
			out.write(":");
			for (int i = 0; i < d.getAttributes().size(); i++) {
				out.write(d.getAttributes().get(i));
				if ((i + 1) < d.getAttributes().size())
					out.write(";");
			}
			out.newLine();
		}
	}

}
