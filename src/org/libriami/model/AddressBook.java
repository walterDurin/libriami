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

package org.libriami.model;

import java.util.ArrayList;
import java.util.List;

public class AddressBook {

	private List<Contact> contacts;

	public AddressBook() {
		contacts = new ArrayList<Contact>();
	}

	public void add(Contact contact) {
		contacts.add(contact);
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	/**
	 * @param contacts List of Contact objects
	 */
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Contact c : contacts) {
			sb.append(c).append("\n");
		}
		return sb.toString();
	}

}
