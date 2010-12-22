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

package org.libriami.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.libriami.model.tools.compare.ApproxMatchable;
import org.libriami.model.tools.compare.StringCompare;

public class EmailAddress implements ApproxMatchable {

	enum Type {
		HOME, WORK
	}

	private final String email;

	public EmailAddress(String email) throws ModelException {
		this.email = email;
		if (!isValid())
			throw new ModelException("Email address is not valid: " + email);
	}

	public String getEmail() {
		return email;
	}

	public boolean isValid() {
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	@Override
	public String toString() {
		return email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailAddress other = (EmailAddress) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public double matchTo(ApproxMatchable o) {
		return StringCompare.compare(email, ((EmailAddress) o).email);
	}

	@Override
	public void merge(ApproxMatchable other) {
		throw new IllegalArgumentException("NYI");
	}

}
