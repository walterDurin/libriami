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

import org.libriami.model.AddressBook;
import org.libriami.model.Contact;
import org.libriami.model.PhoneNumber;

public class FixGermanPhoneNumber {

	public static void fix(AddressBook addressBook) {
		for (Contact c : addressBook.getContacts()) {
			for (PhoneNumber p : c.getPhone()) {
				fix(p);
			}
		}
	}

	public static PhoneNumber fix(PhoneNumber pn) {
		String s = pn.getNumber();

		// Remove all chars except +0123456789
		String allowed = "+0123456789";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (allowed.indexOf(ch) >= 0)
				sb.append(ch);
		}

		if (sb.charAt(0) == '+') {

		} else {

			if (sb.charAt(0) == '0') {
				// 07118 => +497118
				sb.setCharAt(0, '+');
				sb.insert(1, "49");
			} else {
				// 7118 => +497118
				sb.insert(0, "+49");
			}
		}

		// PhoneNumber p = new PhoneNumber(s);
		PhoneNumber p = pn;
		p.setNumber(sb.toString());
		return p;
	}

}
