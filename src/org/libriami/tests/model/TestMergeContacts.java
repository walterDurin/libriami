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

import junit.framework.TestCase;

import org.libriami.model.Address;
import org.libriami.model.Contact;
import org.libriami.model.EmailAddress;
import org.libriami.model.PhoneNumber;

public class TestMergeContacts extends TestCase {

	public void testMergeContacts() throws Exception {

		Contact c1 = new Contact("Test", "Hans");
		c1.getEmail().add(new EmailAddress("hans@test.org"));
		c1.getAddress().add(new Address("Some street 123\nfoo,bar,D-70123 Stuttgart"));

		Contact c2 = new Contact("Test", "Hans");
		c2.getEmail().add(new EmailAddress("hans@test.org"));
		c2.getEmail().add(new EmailAddress("hans2@fooo.de"));
		c2.getPhone().add(new PhoneNumber("+49711545454"));
		c2.getAddress().add(new Address("Some street 123\nfoo,bar,D-70123 Stuttgart"));
		c2.getAddress().add(new Address("Some other street\n,D-70124 Stuttgart"));
		// ContactMerger.merge(c1, c2);

		c1.merge(c2);

		System.out.println(c1);

	}
}
