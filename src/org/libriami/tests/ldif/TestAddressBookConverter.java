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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import junit.framework.TestCase;

import org.libriami.formats.ldif.Decoder;
import org.libriami.formats.ldif.Encoder;
import org.libriami.formats.ldif.LdifElement;
import org.libriami.formats.ldif.LdifFile;
import org.libriami.tests.TestFiles;
import org.libriami.utils.PushBackLineReader;

public class TestAddressBookConverter extends TestCase {

	public void testOne() throws Exception {
		PushBackLineReader in = new PushBackLineReader(new BufferedReader(new FileReader(TestFiles.BIG_EXAMPLE)), false);
		LdifFile lf = Decoder.pullFile(in);
		in.close();

		List<LdifElement> found = lf.find("dn", "uid=admin, dc=structure-net, dc=de");

		if (found != null) {
			for (LdifElement e : found) {
				System.err.println("FOUND:");
				e.dump();
			}
		} else {
			System.err.println("ERR: NOTHING FOUND!");
		}

		File f1 = File.createTempFile("libriami", "junit.ldif");
		System.out.println(f1);
		File f2 = File.createTempFile("libriami", "junit.ldif");
		System.out.println(f1);
		File f3 = File.createTempFile("libriami", "junit.ldif");
		System.out.println(f1);

		BufferedWriter out = new BufferedWriter(new FileWriter(f1));
		Encoder.encodeFile(lf, out);
		out.close();

		in = new PushBackLineReader(new BufferedReader(new FileReader(f2)), false);
		lf = Decoder.pullFile(in);
		in.close();

		out = new BufferedWriter(new FileWriter(f3));
		Encoder.encodeFile(lf, out);
		out.close();
	}
}
