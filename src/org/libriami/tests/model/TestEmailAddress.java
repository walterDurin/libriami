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

import org.libriami.model.EmailAddress;
import org.libriami.model.ModelException;

public class TestEmailAddress extends TestCase {

	public void testEmailEqualCheck() throws ModelException {
		EmailAddress e1 = new EmailAddress("hans@test.org");
		EmailAddress e2 = new EmailAddress("hans@test.org");
		EmailAddress e3 = new EmailAddress("hans2@test.org");

		assertEquals(e1, e2);
		assertEquals(false, e1.equals(e3));
		assertEquals(false, e2.equals(e3));
	}

	public void testValidity() {

		try {
			new EmailAddress("hans@test.org");
			assertTrue(true);
		} catch (ModelException e) {
			assertTrue(true);
		}

		try {
			new EmailAddress("hans@testorg");
			assertTrue(false);
		} catch (ModelException e) {
			assertTrue(true);
		}

		try {
			new EmailAddress("hanstestorg");
			assertTrue(false);
		} catch (ModelException e) {
			assertTrue(true);
		}

		try {
			new EmailAddress("hanstest.org");
			assertTrue(false);
		} catch (ModelException e) {
			assertTrue(true);
		}

		try {
			new EmailAddress("hans@org");
			assertTrue(false);
		} catch (ModelException e) {
			assertTrue(true);
		}

	}

}
