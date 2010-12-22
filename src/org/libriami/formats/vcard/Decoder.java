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

import java.io.BufferedReader;
import java.io.IOException;
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

public class Decoder implements org.libriami.coder.Decoder {

	public AddressBook decode(BufferedReader bin) throws IOException {
		return decode(bin, new ConsoleCoderListener());
	}

	public AddressBook decode(BufferedReader bin, CoderListener listener) throws IOException {
		AddressBook addressBook = new AddressBook();
		PushBackLineReader in = new PushBackLineReader(bin, true);

		VcardElement vc;
		while ((vc = VcardElement.pull(in)) != null) {
			// vc.dump();
			List<String> names = vc.getFirst("N").getValue();

			// http://tools.ietf.org/html/rfc2426#section-3.1.2
			// Family Name, Given Name, Additional Names, Honorific Prefixes,
			// and Honorific Suffixes
			String givenname = "";
			String surname = "";
			if (names != null && names.size() >= 1)
				surname = names.get(0);
			if (names != null && names.size() >= 2)
				givenname = names.get(1);

			Contact contact = new Contact(givenname, surname);

			// BDAY:1971-06-24
			String bday = vc.getFirst("BDAY").getFirstValue();
			if (bday != null) {
				// No support of time in bday so far
				if (bday.indexOf('T') >= 0)
					bday = bday.substring(0, bday.indexOf('T'));

				if (bday.indexOf('-') >= 0) {
					String t[] = bday.split("-");
					if (t.length >= 3) {
						String sday = t[t.length - 1];
						String smonth = t[t.length - 2];
						// String syear = t[t.length - 2]; // FIXME: Year not
						// yet
						// supported!
						int day = Integer.parseInt(sday);
						int month = Integer.parseInt(smonth);
						Birthday birthday = new Birthday(day, month);
						contact.setBirthday(birthday);
					}
				} else {
					// BDAY:20101216
					int year = Integer.parseInt(bday.substring(0, 4));
					int month = Integer.parseInt(bday.substring(4, 6));
					int day = Integer.parseInt(bday.substring(6, 8));
					Birthday birthday = new Birthday(day, month, year);
					contact.setBirthday(birthday);
				}
			}

			// NICKNAME:Robbie
			if (vc.getFirst("NICKNAME") != null)
				contact.setNickname(vc.getFirst("NICKNAME").getFirstValue());

			// TEL;CELL:+491714556926
			List<VcardData> list = vc.get("TEL");
			for (VcardData d : list) {
				PhoneNumber.Type type = getPhoneNumberType(d);
				PhoneNumber pn = new PhoneNumber(d.getFirstValue(), type);
				contact.getPhone().add(pn);
			}

			// ADR;HOME;ENCODING=QUOTED-PRINTABLE
			list = vc.get("ADR");
			for (VcardData d : list) {
				Address.Type type = getAddressType(d);
				Address addr = new Address(d.getValue(), type);
				contact.getAddress().add(addr);
			}

			// EMAIL;PREF;HOME:sabine@w2eweqe.com
			list = vc.get("EMAIL");
			for (VcardData d : list) {
				try {
					EmailAddress email = new EmailAddress(d.getFirstValue());
					contact.getEmail().add(email);
				} catch (ModelException exc) {
					listener.warn(contact, "Skipping email address" + exc.getMessage());
				}
			}

			// URL;WORK:http://webseite1.com
			if (vc.getFirst("URL") != null)
				contact.setUrl(vc.getFirst("URL").getFirstValue());

			addressBook.add(contact);
		}

		return addressBook;
	}

	private org.libriami.model.Address.Type getAddressType(VcardData d) {
		Address.Type type = Address.Type.DEFAULT;
		if (d.getParameter().contains("WORK") || d.getParameter().contains("TYPE=WORK"))
			type = Address.Type.WORK;
		else if (d.getParameter().contains("HOME") || d.getParameter().contains("TYPE=HOME"))
			type = Address.Type.HOME;
		return type;
	}

	private Type getPhoneNumberType(VcardData d) {
		PhoneNumber.Type type = PhoneNumber.Type.DEFAULT;
		if (d.getParameter().contains("WORK") || d.getParameter().contains("TYPE=WORK"))
			type = PhoneNumber.Type.WORK;
		else if (d.getParameter().contains("CELL") || d.getParameter().contains("TYPE=CELL"))
			type = PhoneNumber.Type.CELL; // XXX CELL SUPPORTED IN LDIF TOO?
		else if (d.getParameter().contains("HOME") || d.getParameter().contains("TYPE=HOME"))
			type = PhoneNumber.Type.HOME;
		return type;
	}

}
