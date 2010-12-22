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
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.libriami.coder.EncoderFactory;
import org.libriami.formats.ldif.Decoder;
import org.libriami.formats.ldif.Encoder;
import org.libriami.formats.ldif.LdifData;
import org.libriami.model.Address;
import org.libriami.model.AddressBook;
import org.libriami.model.Birthday;
import org.libriami.model.Contact;
import org.libriami.model.EmailAddress;
import org.libriami.model.PhoneNumber;
import org.libriami.utils.PushBackLineReader;

public class TestLdifData extends TestCase {

	private String encodeToString(LdifData ldd) throws IOException {
		StringWriter sw = new StringWriter();
		BufferedWriter out = new BufferedWriter(sw);
		Encoder.writeLdifData(ldd, out);
		out.flush();
		out.close();
		return sw.toString();
	}

	public void testConstructorAndGetters() {
		LdifData ldd = new LdifData("dn", "dc=structure-net, dc=de");
		assertEquals("dn", ldd.getName());
		assertEquals("dc=structure-net, dc=de", ldd.getValue());
	}

	public void testBase64Value() {
		LdifData ldd = new LdifData("description", "SGVsbG8Kd29ybGQ=", true);
		assertEquals("Hello\nworld", ldd.getValue());
	}

	public void testComplexValue() throws IOException {
		LdifData ldd = new LdifData("cn", "Dietrich\nPfeifle");
		assertEquals("Dietrich\nPfeifle", ldd.getValue());
		assertEquals("cn:: RGlldHJpY2gKUGZlaWZsZQ==", encodeToString(ldd));
	}

	public void testLongValue() throws IOException {
		LdifData ldd = new LdifData(
				"cn",
				"01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
		System.out.println(encodeToString(ldd));
	}

	public void testFoo() throws IOException {
		PushBackLineReader in = new PushBackLineReader(new BufferedReader(new FileReader("tests/example1.ldif")), false);

		LdifData data = null;
		while ((data = Decoder.pullLdifData(in)) != null)
			data.dump();
	}
	
	public void testContact() throws Exception {
		Contact c = new Contact("Bernard", "Börrenser");
		c.setNickname("Bernie");
		c.setBirthday(new Birthday(10, 10));
		c.getAddress().add(new Address("Kläranistraße 8181/a, 17634 Räckerwald"));
		c.getEmail().add(new EmailAddress("bernard.boerrenser@acme.org"));
		c.getPhone().add(new PhoneNumber("+49 2331212 23412313"));
		AddressBook addressBook = new AddressBook();
		addressBook.add(c);
		
		StringWriter sw = new StringWriter();
		BufferedWriter out = new BufferedWriter(sw);
		EncoderFactory.getLdifEncoder().encode(addressBook, out);
		out.flush();
		String s = sw.getBuffer().toString();
		System.out.println(s);
		
		// I used this output to fill some ldfif file.
	}
}
