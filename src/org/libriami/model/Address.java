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

import org.libriami.model.tools.compare.ApproxMatchable;
import org.libriami.model.tools.compare.ListUtil;
import org.libriami.model.tools.compare.StringCompare;

public class Address implements ApproxMatchable {
	public enum Type {
		DEFAULT, HOME, WORK
	}

	private final List<String> lines;
	private final Type type;

	public Address(List<String> lines) {
		this(lines, Type.DEFAULT);
	}

	public Address(List<String> line, Type type) {
		this.type = type;
		this.lines = new ArrayList<String>();
		for (String l : line)
			addLine(l);
	}

	public Address(String address) {
		this.type = Type.DEFAULT;
		this.lines = new ArrayList<String>();
		addLine(address);
	}

	// FIXME: Move to decoder

	private void addLine(String l) {
		l = l.replace("\\n", "\n");
		l = l.replace(",", "\n");
		String t[] = l.split("\n");
		for (String tok : t) {
			tok = tok.trim();
			if (tok.length() > 0)
				this.lines.add(tok);
		}
	}

	public List<String> getLines() {
		return lines;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return lines.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lines == null) ? 0 : lines.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (lines == null) {
			if (other.lines != null)
				return false;
		} else if (!lines.equals(other.lines))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public double matchTo(ApproxMatchable o) {
		return StringCompare.compare(ListUtil.getStringFromList(lines), ListUtil.getStringFromList(((Address) o).lines));
	}

	@Override
	public void merge(ApproxMatchable other) {
		throw new IllegalArgumentException("NYI");
	}

}
