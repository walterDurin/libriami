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

// http://tools.ietf.org/html/rfc2426
// http://tools.ietf.org/html/rfc2426
// PHOTO;VALUE=URL;TYPE=GIF:http://www.site.com/dir_photos/my_photo.gif

public class VcardData {

	private String name;

	private List<String> parameter;

	private List<String> attributes;

	public VcardData() {
		this.name = null;
		this.parameter = new ArrayList<String>();
		this.attributes = new ArrayList<String>();
	}

	public VcardData(String name, String attribute) {
		this.name = name;
		this.parameter = new ArrayList<String>();
		this.attributes = new ArrayList<String>();
		this.attributes.add(attribute);
	}

	public VcardData(String name, String parameters[], String attributes[]) {
		this.name = name;
		this.parameter = new ArrayList<String>();
		this.attributes = new ArrayList<String>();
		for (String p : parameters)
			this.parameter.add(p);
		for (String a : attributes)
			this.attributes.add(a);
	}

	public String getName() {
		return name;
	}

	public String getFirstValue() {
		if (attributes != null && attributes.size() > 0)
			return attributes.get(0);
		else
			return null;
	}

	public List<String> getValue() {
		return attributes;
	}

	public String getValue(int i) {
		if (attributes != null && attributes.size() > i)
			return attributes.get(i);
		else
			return null;
	}

	public List<String> getParameter() {
		return parameter;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public static VcardData pull(PushBackLineReader in) throws IOException {
		String line = in.readLine();
		if (line == null)
			return null;

		if (line.indexOf(':') < 0)
			throw new IOException("Vcard data line requires a : character to separate key and value. Instead we got '" + line + "'");

		boolean endReached = false;
		do {
			String nextLine = in.readLine();
			if (nextLine == null) {
				endReached = true;
			} else {
				boolean emptyLine = nextLine.length() <= 0;

				if ((!emptyLine && nextLine.charAt(0) == ' ') || (!emptyLine && !nextLine.contains(":"))) {
					// Next line belongs to value
					nextLine = nextLine.trim();
					line += nextLine;
				} else {
					in.pushBack(nextLine);
					endReached = true;
				}
			}
		} while (!endReached);

		return pull(line);
	}

	public static VcardData pull(String line) throws IOException {
		// PHOTO;VALUE=URL;TYPE=GIF:http://www.site.com/dir_photos/my_photo.gif
		VcardData vc = new VcardData();

		if (line.indexOf(':') < 0)
			throw new IOException("Illegal line: " + line);

		String left = line.substring(0, line.indexOf(':'));
		String right = line.substring(line.indexOf(':') + 1);

		// vc.parameter = new ArrayList<String>();
		// vc.attributes = new ArrayList<String>();

		// PHOTO;VALUE=URL;TYPE=GIF:http://www.site.com/dir_photos/my_photo.gif

		boolean hasParameter = left.indexOf(';') > 0;
		if (hasParameter) {
			String t[] = left.split(";");
			vc.name = t[0];

			for (int i = 1; i < t.length; i++)
				vc.parameter.add(t[i]);
		} else {
			vc.name = left;
		}

		// ATTRIBUTES
		if (right.indexOf(';') >= 0) {
			String t[] = right.split(";");
			for (int i = 0; i < t.length; i++)
				vc.attributes.add(t[i]);
		} else {
			vc.attributes.add(right);
		}

		// DECODE
		if (vc.parameter.contains("ENCODING=QUOTED-PRINTABLE")) {
			// Decode value as HEX
			// if (!vc.parameter.contains("CHARSET=UTF-8"))
			// throw new IOException("Just UTF-8 encoding supported right now");

			for (int i = 0; i < vc.attributes.size(); i++) {
				byte b[] = HexDecoder.decode(vc.attributes.get(i));
				String tmp = new String(b, "UTF-8");
				tmp = tmp.trim();
				vc.attributes.set(i, tmp);
			}
		}

		return vc;
	}

	public void dump() {
		System.out.print(getName());

		for (int i = 0; i < getParameter().size(); i++) {
			System.out.print(";" + getParameter().get(i));
		}

		System.out.print(":");

		for (int i = 0; i < getAttributes().size(); i++) {
			if (i > 0)
				System.out.print(";");

			System.out.print(getAttributes().get(i));
		}
		System.out.println();
	}

}
