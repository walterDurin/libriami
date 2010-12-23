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

public class LdifElement {

	private final List<LdifData> lines = new ArrayList<LdifData>();

	public void add(LdifData data) {
		lines.add(data);
	}

	public void dump() {
		for (LdifData data : lines) {
			data.dump();
		}
	}

	public List<LdifData> find(String name, String value) {
		List<LdifData> found = null;
		for (LdifData data : lines) {
			if (name.equalsIgnoreCase(data.getName())) {
				if (data.getValue().contains(value)) {
					if (found == null)
						found = new ArrayList<LdifData>();
					found.add(data);
				}
			}
		}
		return found;
	}

	public LdifData getFirst(String name) {
		for (LdifData data : lines) {
			if (name.equalsIgnoreCase(data.getName())) {
				return data;
			}
		}
		return null;
	}

	public List<LdifData> get(String name) {
		List<LdifData> searchList = new ArrayList<LdifData>();
		for (LdifData data : lines) {
			if (name.equalsIgnoreCase(data.getName())) {
				searchList.add(data);
			}
		}
		return searchList;
	}

	public boolean isEmpty() {
		return lines.size() == 0;
	}

	public List<LdifData> getData() {
		return lines;
	}
}
