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

import org.libriami.model.Address;
import org.libriami.model.Contact;
import org.libriami.model.EmailAddress;
import org.libriami.model.PhoneNumber;

/**
 * @deprecated
 */
@Deprecated
public class ContactMerger {

	public static void merge(Contact c1, Contact c2) {
		if (c1 == c2 || c1.equals(c2))
			return;

		// Email addresses
		for (EmailAddress e : c2.getEmail()) {
			if (c1.getEmail().contains(e) == false)
				c1.getEmail().add(e);
		}

		// Phone numbers
		for (PhoneNumber p : c2.getPhone()) {
			if (c1.getPhone().contains(p) == false)
				c1.getPhone().add(p);
		}

		// Addresses
		for (Address a : c2.getAddress()) {
			if (c1.getAddress().contains(a) == false) {
				c1.getAddress().add(a);
			}
		}

		// Birthday
		if (c2.getBirthday() != null && c1.getBirthday() == null) {
			c1.setBirthday(c2.getBirthday());
		}

		// URL
		if (c2.getUrl() != null && c1.getUrl() == null) {
			c1.setUrl(c2.getUrl());
		}

		// Nickname
		if (c2.getNickname() != null && c1.getNickname() == null) {
			c1.setNickname(c2.getNickname());
		}

	}

}
