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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * http://tools.ietf.org/html/rfc2849
 */
public class LdifFile {

	List<LdifElement> elements = new ArrayList<LdifElement>();

	public List<LdifElement> getElements() {
		return elements;
	}

	public void dump() {
		for (LdifElement d : elements) {
			d.dump();
			System.out.println();
		}
	}

	public List<LdifElement> find(String name, String value) {
		List<LdifElement> found = null;

		for (LdifElement e : elements) {
			List<LdifData> f = e.find(name, value);
			if (f != null) {
				if (found == null)
					found = new ArrayList<LdifElement>();
				found.add(e);
			}
		}

		return found;
	}

	public void add(LdifElement e) {
		elements.add(e);
	}

}
