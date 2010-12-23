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

package org.libriami.tests.ldif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.libriami.formats.ldif.Decoder;
import org.libriami.formats.ldif.LdifData;
import org.libriami.formats.ldif.LdifElement;
import org.libriami.utils.PushBackLineReader;

public class TestLdifElement extends TestCase {

	public void testFoo() throws IOException {
		PushBackLineReader in = new PushBackLineReader(new BufferedReader(new FileReader("tests/example1.ldif")), false);

		LdifElement dataSet = null;
		while ((dataSet = Decoder.pullElement(in)) != null) {
			dataSet.dump();
			System.out.println();
		}

	}

	public void testMultiData() throws IOException {
		LdifElement e = new LdifElement();
		e.add(new LdifData("objectclass", "top"));
		e.add(new LdifData("objectclass", "person"));
		e.add(new LdifData("objectclass", "organizationalPerson"));
		e.add(new LdifData("cn", "Barbara Jensen"));

		List<LdifData> l = e.get("objectclass");
		assertEquals(3, l.size());
		assertEquals("top", l.get(0).getValue());
		assertEquals("person", l.get(1).getValue());
		assertEquals("organizationalPerson", l.get(2).getValue());

		l = e.get("foo");
		assertEquals(0, l.size());

		l = e.get("cn");
		assertEquals(1, l.size());
		assertEquals("Barbara Jensen", l.get(0).getValue());

	}
}
