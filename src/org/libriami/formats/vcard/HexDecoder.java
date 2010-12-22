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

package org.libriami.formats.vcard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.codec.DecoderException;

public class HexDecoder {

	public static byte[] decode(String s) throws IOException {
		try {
			// =47=C3=B6=74=74=6C=69=63=68=65=72
			// LABEL;HOME;ENCODING=QUOTED-PRINTABLE:Street=3DStreet=0D=0A7000
			// Home=0D=0ADeutschland
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			StringReader in = new StringReader(s);
			int k;
			while ((k = in.read()) >= 0) {
				char ch = (char) k;
				if (ch == '=') {
					// Decode hex byte
					int tmp = decodeHexString(in) & 0xff;
					out.write(tmp);
				} else {
					// Could be unicode and multibytes...
					// XXX Do we need to specify UTF-8, improve performance!
					out.write(new String(new char[] { ch }).getBytes());
				}
			}
			// out.close();
			return out.toByteArray();
			//
			// for (int i = 0; i < s.length(); i++) {
			// char ch = s.charAt(i);
			// if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f')
			// || (ch >= 'A' && ch <= 'F'))
			// out.append(ch);
			// }
			// char c[] = out.toCharArray();
			// return Hex.decodeHex(c);
			// // s = s.replaceAll("=", "");
			// // s = s.replaceAll(" ", "");
			// // return Hex.decodeHex(s.toCharArray());
		} catch (DecoderException e) {
			throw new IOException("Decoding hex failed " + e);
		}
	}

	private static int decodeHexString(StringReader in) throws DecoderException, IOException {
		return decodeHex(in.read(), in.read());
	}

	private static int decodeHex(int hb, int lb) throws IOException, DecoderException {
		if (hb < 0 || lb < 0)
			throw new IOException("End of stream reached. Could not complete hex byte");
		return decodeNibble(hb & 0xff) << 4 | decodeNibble(lb & 0xff);
	}

	private static int decodeNibble(final int ch) throws DecoderException {
		if (ch >= '0' && ch <= '9') {
			return (ch - '0');
		} else if (ch >= 'a' && ch <= 'f') {
			return (ch - 'a') + 10;
		} else if (ch >= 'A' && ch <= 'F') {
			return (ch - 'A') + 10;
		} else {
			throw new DecoderException("No hex char: " + ch);
		}
	}

	public static void main(String args[]) throws Exception {
		System.out.println(decodeNibble('0'));
	}
}
