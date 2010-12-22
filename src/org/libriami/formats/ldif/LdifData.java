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

package org.libriami.formats.ldif;

import java.io.UnsupportedEncodingException;

/**
 * 
 * http://www.iana.org/assignments/ldap-parameters
 * 
 */
public class LdifData {

	private String name;
	private String value;
	private boolean isBase64Encoded;

	public LdifData(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public LdifData(String name, String value, boolean isBase64Encoded) {
		this.name = name;
		this.isBase64Encoded = isBase64Encoded;
		byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(value);
		// this.value = new String(b);
		try {
			this.value = new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (isBase64Encoded)
			return name + ":: " + value;
		else
			return name + ": " + value;
	}

	public void dump() {
		System.out.println(toString());
	}

}
