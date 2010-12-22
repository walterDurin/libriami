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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.libriami.utils.PushBackLineReader;

public class VcardElement {

	private static final VcardData NIL = new VcardData();

	List<VcardData> data = new ArrayList<VcardData>();

	public VcardData getFirst(String name) {
		for (VcardData d : data) {
			if (name.equals(d.getName()))
				return d;
		}
		return NIL;
	}

	public List<VcardData> get(String name) {
		List<VcardData> l = new ArrayList<VcardData>();
		for (VcardData d : data) {
			if (name.equals(d.getName()))
				l.add(d);
		}
		return l;
	}

	public static VcardElement pull(PushBackLineReader in) throws IOException {
		VcardData data;
		VcardElement element = new VcardElement();

		data = VcardData.pull(in);
		if (data == null)
			return null;

		if ("BEGIN".equals(data.getName()) && "VCARD".equals(data.getFirstValue())) {
			element.data.add(data);
		} else {
			throw new IOException("Begin marker missing BEGIN:VCARD");
		}

		while ((data = VcardData.pull(in)) != null) {
			if ("END".equals(data.getName()) && "VCARD".equals(data.getFirstValue())) {
				element.data.add(data);
				return element;
			} else {
				if (element != null)
					element.data.add(data);
			}
		}
		throw new IOException("End marker missing END:VCARD");
	}

	public void dump() {
		for (VcardData d : data) {
			d.dump();
		}
	}

	public void add(String name, String value) {
		data.add(new VcardData(name, value));

	}

	public void add(VcardData vd) {
		data.add(vd);
	}

	public List<VcardData> getData() {
		return data;
	}
}
