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

package org.libriami.tests.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.libriami.model.tools.FixDuplicates;
import org.libriami.model.tools.compare.ApproxMatchable;

/**
 * 
 */
public class TestFixDuplicates extends TestCase {

	public void testNewGeneric() {
		Person a = new Person("A", "B");
		Person b = new Person("C", "D");
		List<Person> l = new ArrayList<Person>();
		l.add(a);
		l.add(b);

		FixDuplicates<Person> fix = new FixDuplicates<Person>();
		l = fix.fix(l);

		assertEquals("AC", a.a);
		assertEquals("BD", a.b);
		assertEquals(1, l.size());
		assertEquals(a, l.get(0));
	}

	private static class Person implements ApproxMatchable {
		String a;
		String b;

		public Person(String a, String b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public double matchTo(ApproxMatchable other) {
			return 1;
		}

		@Override
		public void merge(ApproxMatchable other) {
			Person o = (Person) other;
			this.a += o.a;
			this.b += o.b;
		}

		@Override
		public String toString() {
			return "a=" + a + " b=" + b;
		}
	}
}
