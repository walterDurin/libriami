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

package org.libriami.tests.vcard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.libriami.formats.vcard.VcardElement;
import org.libriami.utils.PushBackLineReader;

public class TestVcardFile extends TestCase {

	public void testOne() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("tests/example1.vcf"));
		VcardElement vc = VcardElement.pull(new PushBackLineReader(in, true));
		vc.dump();
	}

	// public void testTwo() throws IOException {
	// BufferedReader in = new BufferedReader(new FileReader(
	// "tests/export_android.vcf"));
	//
	// VcardElement vc;
	// PushBackLineReader reader = new PushBackLineReader(in, true);
	// while ((vc = VcardElement.pull(reader)) != null) {
	// vc.dump();
	// }
	// }

	public void testThree() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("tests/format_outlook.vcf"));
		VcardElement vc = VcardElement.pull(new PushBackLineReader(in, true));
		vc.dump();
	}

}
