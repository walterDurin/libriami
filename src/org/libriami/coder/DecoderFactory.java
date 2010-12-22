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

package org.libriami.coder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class DecoderFactory {

	public final static Decoder getLdifDecoder() {
		return new org.libriami.formats.ldif.Decoder();
	}

	public final static Decoder getVcardDecoder() {
		return new org.libriami.formats.vcard.Decoder();
	}

	public final static org.libriami.coder.Decoder getDecoder(File f) throws IOException {
		String suffix = f.getName().toLowerCase();
		if (suffix.endsWith(".ldif")) {
			return getLdifDecoder();
		} else if (suffix.endsWith(".vcf")) {
			return getVcardDecoder();
		} else {
			throw new IOException("File extension not known (.ldif, .vcf)");
		}
	}

	/**
	 * Creates a UTF-8 buffered input reader from a file.
	 */
	public static BufferedReader getUTF8Reader(File f) throws UnsupportedEncodingException, FileNotFoundException {
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(f), "UTF-8");
		BufferedReader in = new BufferedReader(inputStreamReader);
		return in;
	}
}
