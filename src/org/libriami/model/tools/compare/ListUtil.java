/*
 * Copyright libriami / 2010 Dietrich Pfeifle
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

import java.util.ArrayList;
import java.util.List;

import org.libriami.model.Settings;

/**
 * 
 */
public final class ListUtil {

	/**
	 * Compares lists of objects with a different one. If at least one object matches (equals=true) it returns true.
	 * 
	 * @param l1 List one
	 * @param l2 List two
	 * @return True if at least one element matches to one of the other list
	 */
	public static <T extends Object> boolean matchesAtLeastOneElement(List<T> l1, List<T> l2) {
		for (Object o1 : l1) {
			for (Object o2 : l2) {
				if (o1.equals(o2)) {
					// One Email matches, return true
					// System.out.println("email match");
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param address
	 * @param address2
	 */
	public static <T extends Object> List<T> mergeLists(List<T> l1, List<T> l2) {
		List<T> result = new ArrayList<T>();
		result.addAll(l1);
		for (T o : l2) {
			if (!l1.contains(o))
				result.add(o);
		}
		return result;
	}

	/**
	 * @param address
	 * @param address2
	 */
	public static <T extends ApproxMatchable> List<T> mergeListsApprox(List<T> l1, List<T> l2) {
		List<T> result = new ArrayList<T>();
		result.addAll(l1);

		for (T o2 : l2) {
			boolean found = false;
			for (T o1 : l1) {
				if (o1.matchTo(o2) > Settings.EQUAL_RATIO) {
					found = true;
					break;
				}
			}
			if (!found)
				result.add(o2);
		}
		// for (T o : l2) {
		// boolean found = false;
		//
		// if (o1.matchTo(o) > Settings.EQUAL_RATIO) {
		// result.add(o);
		// break;
		// }
		// }
		// }
		return result;
	}

	public static String getStringFromList(List<String> l) {
		StringBuilder sb = new StringBuilder();
		for (String s : l) {
			sb.append(s);
		}
		String s = sb.toString();
		// s = s.replaceAll(" ", ""); // TODO: Trim more chars and more efficiently
		return s;
	}
}
