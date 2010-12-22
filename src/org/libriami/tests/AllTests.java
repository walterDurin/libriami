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

package org.libriami.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.libriami.tests.ldif.TestAddressBookConverter;
import org.libriami.tests.ldif.TestExample5File;
import org.libriami.tests.ldif.TestLdifData;
import org.libriami.tests.ldif.TestLdifElement;
import org.libriami.tests.ldif.TestLdifFile;
import org.libriami.tests.model.TestBirthday;
import org.libriami.tests.model.TestBirthdayList;
import org.libriami.tests.model.TestDuplicates;
import org.libriami.tests.model.TestEmailAddress;
import org.libriami.tests.model.TestFixDuplicates;
import org.libriami.tests.model.TestLdifDecodeEncodeCycle;
import org.libriami.tests.model.TestLdifDecoder;
import org.libriami.tests.model.TestLdifToVcard;
import org.libriami.tests.model.TestMergeContacts;
import org.libriami.tests.model.TestVcardDecoder;
import org.libriami.tests.vcard.TestVcardData;
import org.libriami.tests.vcard.TestVcardFile;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());

		suite.addTestSuite(TestAddressBookConverter.class);
		suite.addTestSuite(TestExample5File.class);
		suite.addTestSuite(TestLdifData.class);
		suite.addTestSuite(TestLdifElement.class);
		suite.addTestSuite(TestLdifFile.class);

		suite.addTestSuite(TestVcardData.class);
		suite.addTestSuite(TestVcardFile.class);

		suite.addTestSuite(TestBirthday.class);
		suite.addTestSuite(TestBirthdayList.class);
		suite.addTestSuite(TestDuplicates.class);
		suite.addTestSuite(TestEmailAddress.class);
		suite.addTestSuite(TestFixDuplicates.class);
		suite.addTestSuite(TestLdifDecodeEncodeCycle.class);
		suite.addTestSuite(TestLdifDecoder.class);
		suite.addTestSuite(TestLdifToVcard.class);
		suite.addTestSuite(TestMergeContacts.class);
		suite.addTestSuite(TestVcardDecoder.class);

		return suite;
	}
}
