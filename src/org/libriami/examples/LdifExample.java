/*
 * Copyright libriami / 2010 Dietrich Pfeifle
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

package org.libriami.examples;

import java.io.BufferedReader;
import java.io.FileReader;

import org.libriami.coder.DecoderFactory;
import org.libriami.model.AddressBook;

/**
 * Small LDIF reading example.
 */
public class LdifExample {

	public static void main(String[] args) throws Exception {

		// Create a buffered reader of sample file
		BufferedReader in = new BufferedReader(new FileReader("tests/example1.ldif"));

		// Create a LDIF decoder and parse the stream
		AddressBook addressBook = DecoderFactory.getLdifDecoder().decode(in);
		in.close();

		// Dump contacts
		System.out.println(addressBook);

		// Use model to access data, i.e. mail address
		String email = addressBook.getContacts().get(0).getEmail().get(0).getEmail();
		System.out.println("First contact has mail addres: " + email);

	}

}
