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

package org.libriami.tests.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.libriami.coder.Decoder;
import org.libriami.coder.DecoderFactory;
import org.libriami.coder.EncoderFactory;
import org.libriami.model.AddressBook;
import org.libriami.tests.TestFiles;

public class TestLdifDecodeEncodeCycle extends TestCase {

	public void testLdifContactsFromFile() throws Exception {

		Decoder decoder = DecoderFactory.getLdifDecoder();

		BufferedReader in = new BufferedReader(new FileReader(TestFiles.BIG_EXAMPLE));
		AddressBook addressBook = decoder.decode(in);
		in.close();

		File f1 = File.createTempFile("libriami", "junit.ldif");
		System.out.println(f1);
		BufferedWriter out = new BufferedWriter(new FileWriter(f1));
		EncoderFactory.getLdifEncoder().encode(addressBook, out);
		out.close();

		in = new BufferedReader(new FileReader(f1));
		addressBook = decoder.decode(in);
		in.close();

		// FixGermanPhoneNumber.fix(addressBook);

		File f2 = File.createTempFile("libriami", "junit.ldif");
		System.out.println(f1);
		out = new BufferedWriter(new FileWriter(f2));
		EncoderFactory.getLdifEncoder().encode(addressBook, out);
		out.close();

		assertEquals(true, compareFiles(f1, f2));
	}

	private boolean compareFiles(File f1, File f2) {
		if (f1.length() != f2.length())
			return false;
		FileInputStream in1 = null, in2 = null;
		try {
			in1 = new FileInputStream(f1);
			in2 = new FileInputStream(f2);

			int k1, k2;

			do {
				k1 = in1.read();
				k2 = in2.read();
				if (k1 != k2) {
					in1.close();
					in2.close();
					return false;
				}
			} while (k1 < 0 || k2 < 0);

			System.out.println(f1.length() + " bytes compared. They are equal.");

			in1.close();
			in2.close();
			return true;
		} catch (Exception e) {
			try {
				if (in1 != null)
					in1.close();
				if (in2 != null)
					in2.close();
			} catch (IOException e1) {
			}
			return false;
		}
	}

}
