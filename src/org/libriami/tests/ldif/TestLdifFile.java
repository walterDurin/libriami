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
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import junit.framework.TestCase;

import org.libriami.formats.ldif.Decoder;
import org.libriami.formats.ldif.LdifFile;
import org.libriami.utils.PushBackLineReader;

public class TestLdifFile extends TestCase {

	public void testReadAllFiles() throws IOException {

		File dir = new File("tests");
		File f = new File(dir, ".");
		String[] list = f.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".ldif");
			}
		});

		for (String s : list) {
			f = new File(dir, s);
			PushBackLineReader in = new PushBackLineReader(new BufferedReader(new FileReader(f)), false);

			System.out.println(">>> File: " + f);

			LdifFile lf = Decoder.pullFile(in);
			lf.dump();
			in.close();
		}
	}
}
