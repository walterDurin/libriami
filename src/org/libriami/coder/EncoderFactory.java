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

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncoderFactory {

	final static Logger LOG = LoggerFactory.getLogger(EncoderFactory.class);

	public final static org.libriami.formats.ldif.Encoder getLdifEncoder() {
		LOG.debug("Creating LDIF Encoder");
		return new org.libriami.formats.ldif.Encoder();
	}

	public final static org.libriami.formats.vcard.Encoder getVcardEncoder() {
		LOG.debug("Creating vCard Encoder");
		return new org.libriami.formats.vcard.Encoder();
	}

	public final static org.libriami.coder.Encoder getDecoder(File f) throws IOException {
		String suffix = f.getName().toLowerCase();
		if (suffix.endsWith(".ldif")) {
			return getLdifEncoder();
		} else if (suffix.endsWith(".vcf")) {
			return getVcardEncoder();
		} else {
			throw new IOException("File extension not known (.ldif, .vcf)");
		}
	}
}
