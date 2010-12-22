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

import java.io.IOException;

import junit.framework.TestCase;

import org.libriami.formats.vcard.VcardData;

public class TestVcardData extends TestCase {

	public void testOne() throws IOException {
		VcardData vc = VcardData.pull("PHOTO;VALUE=URL;TYPE=GIF:http://www.site.com/dir_photos/my_photo.gif");
		assertEquals("PHOTO", vc.getName());
		assertEquals(2, vc.getParameter().size());
		assertEquals("VALUE=URL", vc.getParameter().get(0));
		assertEquals("TYPE=GIF", vc.getParameter().get(1));
		assertEquals(1, vc.getAttributes().size());
		assertEquals("http://www.site.com/dir_photos/my_photo.gif", vc.getAttributes().get(0));
	}

	public void testTwo() throws IOException {
		VcardData vc;

		vc = VcardData.pull("BEGIN:VCARD");
		assertEquals("BEGIN", vc.getName());
		assertEquals(0, vc.getParameter().size());
		assertEquals(1, vc.getAttributes().size());
		assertEquals("VCARD", vc.getAttributes().get(0));
	}

	public void testEncoded() throws IOException {
		VcardData vc = VcardData.pull("N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=47=C3=B6=74=74=6C=69=63=68=65=72;=48=61=72=61=6C=64;;;");
		assertEquals("N", vc.getName());
		assertEquals("CHARSET=UTF-8", vc.getParameter().get(0));
		assertEquals("ENCODING=QUOTED-PRINTABLE", vc.getParameter().get(1));
		assertEquals("Göttlicher", vc.getValue(0));
	}

	public void testEncoded2() throws IOException {
		// LABEL;WORK;ENCODING=QUOTED-PRINTABLE:Buero=0D=0AStreet=0D=0AAdress
		// work. 7000=0D=0ADeutschland
		VcardData vc = VcardData.pull("LABEL;WORK;ENCODING=QUOTED-PRINTABLE:Buero=0D=0AStreet=0D=0AAdress work. 7000=0D=0ADeutschland");
		assertEquals("LABEL", vc.getName());
		assertEquals("WORK", vc.getParameter().get(0));
		assertEquals("ENCODING=QUOTED-PRINTABLE", vc.getParameter().get(1));
		assertEquals("Buero\r\nStreet\r\nAdress work. 7000\r\nDeutschland", vc.getValue(0));
	}

}
