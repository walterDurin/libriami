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

package org.libriami.model.tools.compare;

import java.util.List;

import org.libriami.model.Settings;

public class StringCompare {

	/**
	 * Returns true if both strings match. They are compared approximately (using the Levenshtein distance algorithm). If the calculated
	 * ratio is above a configurable value it returns true.
	 * 
	 * @param s1 First string to compare
	 * @param s2 Second string to compare
	 * @return True if both strings are nearly the same
	 */
	public static boolean nearlyTheSame(String s1, String s2) {
		return compare(s1, s2) > Settings.EQUAL_RATIO;
	}

	/**
	 * Returns true if both strings match. They are compared approximately (using the Levenshtein distance algorithm). If the calculated
	 * ratio is above a configurable value it returns true.
	 * 
	 * @param s1 First string to compare
	 * @param s2 Second string to compare
	 * @return True if both strings are nearly the same
	 */
	public static boolean nearlyTheSame(List<String> s1, List<String> s2) {
		StringBuilder sb1 = new StringBuilder();
		for (String s : s1)
			sb1.append(s);

		StringBuilder sb2 = new StringBuilder();
		for (String s : s2)
			sb2.append(s);

		return compare(sb1.toString(), sb2.toString()) > Settings.EQUAL_RATIO;
	}

	public static double compare(String a, String b) {
		if (a == null || b == null) {
			return (a == b) ? 1.0 : 0.0;
		} else {
			if (a == b || a.equals(b)) {
				return 1.0;
			} else if (a.equals("")) {
				return b.equals("") ? 1.0 : 0.0;
			} else {
				a = a.replaceAll(" ", ""); // FIXME: Improvements welcome...
				b = b.replaceAll(" ", "");

				int max = Math.max(a.length(), b.length());
				int ld;

				if (Settings.IGNORECASE_STRING_COMPARE)
					ld = LevenshteinDistance.computeLevenshteinDistance(a.toLowerCase(), b.toLowerCase());
				else
					ld = LevenshteinDistance.computeLevenshteinDistance(a, b);

				return 1 - (double) ld / max;
			}
		}
	}

}
