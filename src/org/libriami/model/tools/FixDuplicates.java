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

import org.libriami.model.Settings;
import org.libriami.model.tools.compare.ApproxMatchable;

public class FixDuplicates<T extends ApproxMatchable> {

	private static class Duplicate {
		public Duplicate(int idx1, int idx2) {
			this.idx1 = idx1;
			this.idx2 = idx2;
		}

		int idx1;
		int idx2;
	}

	/**
	 * Fixes duplicates in given list.
	 * 
	 * @param source List to work on
	 * @return New instance a list containing fixed items
	 */
	public List<T> fix(List<T> source) {

		List<T> l = new ArrayList<T>();
		l.addAll(source);

		for (T t : l)
			System.err.println("> " + t);

		Set<Integer> removals = new HashSet<Integer>();

		List<Duplicate> duplicates = findDuplicates(l);
		duplicates = sortDuplicates(duplicates);

		// Now merge the duplicates. This will merge all non-existing fields from contact2 to contact1. So we can delete contact2 later on.
		for (Duplicate d : duplicates) {
			T t1 = l.get(d.idx1);
			T t2 = l.get(d.idx2);
			t1.merge(t2);
			// System.err.println("Merged " + d.idx2 + " -> " + d.idx1);
			removals.add(d.idx2);
		}

		// Remove from back not to get IndexOutOfBounds cause indices will change
		ArrayList<Integer> list = new ArrayList<Integer>(removals);
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			int idx = list.get(list.size() - i - 1);
			l.remove(idx);
			// System.err.println("Removed " + idx);
		}

		// for (T t : l)
		// System.err.println("> " + t);

		return l;
	}

	/**
	 * Creates a list of duplicates of given contact list.
	 * 
	 * @param contacts List of contacts to check
	 * @return list of duplicates of given contact list
	 */
	private List<Duplicate> findDuplicates(List<T> l) {
		// Traverse list and compare against all elements once. This could be done by comparing one item with all on the right side.
		List<Duplicate> duplicates = new ArrayList<Duplicate>();
		for (int i = 0; i < l.size() - 1; i++) {
			for (int j = (i + 1); j < l.size(); j++) {

				T c1 = l.get(i);
				T c2 = l.get(j);
				double ratio = c1.matchTo(c2);
				// System.err.println(i + " <-> " + j + " -- " + ratio);

				// System.err.println("Ratio: " + ratio);
				if (ratio > Settings.EQUAL_RATIO) {
					// System.err.println("Duplicate found (ration:" + ratio + "):" + i + " " + j);
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
	private List<Duplicate> sortDuplicates(List<Duplicate> duplicates) {
		Collections.sort(duplicates, new Comparator<Duplicate>() {
			@Override
			public int compare(Duplicate o1, Duplicate o2) {
				// Descending order
				return o2.idx2 - o1.idx2;
			}
		});
		return duplicates;
	}

}
