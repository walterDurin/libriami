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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.libriami.model.AddressBook;
import org.libriami.model.Contact;
import org.libriami.model.EmailAddress;
import org.libriami.model.Settings;
import org.libriami.model.tools.compare.StringCompare;

// FIXME: Replace by a generic solution. See class FixDuplicates in compare package

/**
 * @deprecated
 */
@Deprecated
public class FixDuplicateContacts {

	static class Duplicate {
		public Duplicate(int idx1, int idx2) {
			this.idx1 = idx1;
			this.idx2 = idx2;
		}

		int idx1;
		int idx2;
	}

	/**
	 * Fixes duplicates in given address book.
	 * 
	 * @param addressBook Address book to fix duplicates
	 * @return New instance of an address book containing cleaned up contacts
	 */
	public static AddressBook fix(AddressBook addressBook) {
		AddressBook newAddressBook = new AddressBook();
		newAddressBook.getContacts().addAll(addressBook.getContacts());

		List<Contact> contacts = newAddressBook.getContacts();

		for (Contact c : addressBook.getContacts())
			System.err.println("> " + c);

		Set<Integer> removals = new HashSet<Integer>();

		List<Duplicate> duplicates = findDuplicates(contacts);
		duplicates = sortDuplicates(duplicates);

		// Now merge the duplicates. This will merge all non-existing fields from contact2 to contact1. So we can delete contact2 later on.
		for (Duplicate d : duplicates) {
			ContactMerger.merge(contacts.get(d.idx1), contacts.get(d.idx2));
			System.err.println("Merged " + d.idx2 + " -> " + d.idx1);
			removals.add(d.idx2);
		}

		// Remove from back not to get IndexOutOfBounds cause indices will change
		ArrayList<Integer> list = new ArrayList<Integer>(removals);
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			int idx = list.get(list.size() - i - 1);
			contacts.remove(idx);
			System.err.println("Removed " + idx);
		}

		return newAddressBook;
	}

	/**
	 * Creates a list of duplicates of given contact list.
	 * 
	 * @param contacts List of contacts to check
	 * @return list of duplicates of given contact list
	 */
	private static List<Duplicate> findDuplicates(List<Contact> contacts) {
		// Traverse list and compare against all elements once. This could be done by comparing one item with all on the right side.
		List<Duplicate> duplicates = new ArrayList<FixDuplicateContacts.Duplicate>();
		for (int i = 0; i < contacts.size() - 1; i++) {
			for (int j = (i + 1); j < contacts.size(); j++) {

				Contact c1 = contacts.get(i);
				Contact c2 = contacts.get(j);
				double ratio = matches(c1, c2);
				// System.err.println(i + " <-> " + j + " -- " + ratio);

				// System.err.println("Ratio: " + ratio);
				if (ratio > Settings.EQUAL_RATIO) {
					System.err.println("Duplicate found (ration:" + ratio + "):" + i + " " + j);
					duplicates.add(new Duplicate(i, j));
				}
			}
		}
		return duplicates;
	}

	/**
	 * Sorts the duplicates in descending order to merge back all items from the back side of the list to front.
	 * 
	 * @param duplicates
	 * @return Ordered duplicates list ready to work on (merge changes)
	 */
	private static List<Duplicate> sortDuplicates(List<Duplicate> duplicates) {
		Collections.sort(duplicates, new Comparator<Duplicate>() {
			@Override
			public int compare(Duplicate o1, Duplicate o2) {
				// Descending order
				return o2.idx2 - o1.idx2;
			}
		});
		return duplicates;
	}

	/**
	 * Returns match ratio of two contacts. 0 is none at all and 1 is fully equal.
	 * 
	 * @param c1 Contact 1
	 * @param c2 Contact 2
	 * 
	 * @return Ration from 0..1. 0 is none at all and 1 is fully equal.
	 */
	private static double matches(Contact c1, Contact c2) {
		double hits = 0;
		double maxhits = 2;

		hits += nearlyTheSame(c1.getSurname(), c2.getSurname());
		hits += nearlyTheSame(c1.getGivenname(), c2.getGivenname());

		if (c1.getEmail().size() > 0 && c2.getEmail().size() > 0) {
			maxhits += 1d;
			if (matchesAtLeastOneMailAddress(c1.getEmail(), c2.getEmail()))
				hits += 1d;
		}

		return hits / maxhits;
	}

	/**
	 * Compares lists of email addresses with a different one. If at least one email address matches it returns true.
	 * 
	 * @param emails1
	 * @param emails2
	 * @return
	 */
	private static boolean matchesAtLeastOneMailAddress(List<EmailAddress> emails1, List<EmailAddress> emails2) {
		for (EmailAddress e1 : emails1) {
			for (EmailAddress e2 : emails2) {
				if (e1.equals(e2)) {
					// One Email matches, return true
					// System.out.println("email match");
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if both strings match. They are compared approximately (using the Levenshtein distance algorithm). If the calculated
	 * ratio is above a configurable value it returns true.
	 * 
	 * @param s1 First string to compare
	 * @param s2 Second string to compare
	 * @return True if both strings are nearly the same
	 */
	private static double nearlyTheSame(String s1, String s2) {
		return StringCompare.compare(s1, s2);// > Settings.EQUAL_RATIO;
	}

}
