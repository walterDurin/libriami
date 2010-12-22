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

import java.util.ArrayList;
import java.util.List;

import org.libriami.model.tools.compare.ApproxMatchable;
import org.libriami.model.tools.compare.ListUtil;
import org.libriami.model.tools.compare.StringCompare;

public class Contact implements ApproxMatchable, Comparable<Contact> {

	static long ids = 0;

	long id;

	/** Givenname, First name, Forename, Vorname */
	private String givenname;

	/** Surname, Family name, Nachname */
	private String surname;

	/** Nickname */
	private String nickname;

	private List<Address> address = new ArrayList<Address>();
	private List<EmailAddress> email = new ArrayList<EmailAddress>();
	private List<PhoneNumber> phone = new ArrayList<PhoneNumber>();
	private Birthday birthday;
	private String url;

	public Contact(String givenname, String surname) {
		this.givenname = givenname;
		this.surname = surname;
		id = (ids++);
	}

	public String getGivenname() {
		return givenname;
	}

	public String getSurname() {
		return surname;
	}

	public void setBirthday(Birthday birthday) {
		this.birthday = birthday;
	}

	public Birthday getBirthday() {
		return birthday;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<PhoneNumber> getPhone() {
		return phone;
	}

	public List<Address> getAddress() {
		return address;
	}

	public List<EmailAddress> getEmail() {
		return email;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(id + " ");

		sb.append(givenname + " " + surname);

		if (birthday != null)
			sb.append(" Birthday: " + birthday);

		if (nickname != null)
			sb.append(" Nickname: " + nickname);

		sb.append(" Address: " + address);

		sb.append(" Email: " + email);

		sb.append(" Phone: " + phone);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((givenname == null) ? 0 : givenname.hashCode());
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Contact other = (Contact) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (givenname == null) {
			if (other.givenname != null)
				return false;
		} else if (!givenname.equals(other.givenname))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public double matchTo(ApproxMatchable other) {
		double hits = 0;
		double maxhits = 2;

		Contact c1 = this;
		Contact c2 = (Contact) other;

		hits += StringCompare.compare(c1.getSurname(), c2.getSurname());
		hits += StringCompare.compare(c1.getGivenname(), c2.getGivenname());

		if (c1.getEmail().size() > 0 && c2.getEmail().size() > 0) {
			maxhits += 1d;
			if (ListUtil.matchesAtLeastOneElement(c1.getEmail(), c2.getEmail()))
				hits += 1d;
		}

		return hits / maxhits;
	}

	@Override
	public void merge(ApproxMatchable other) {
		Contact o = (Contact) other;
		if (givenname == null || givenname.length() == 0)
			givenname = o.givenname;
		if (surname == null || surname.length() == 0)
			surname = o.surname;
		if (nickname == null || nickname.length() == 0)
			nickname = o.nickname;
		address = ListUtil.mergeListsApprox(address, o.address);
		email = ListUtil.mergeListsApprox(email, o.email);
		phone = ListUtil.mergeListsApprox(phone, o.phone);
		if (birthday == null || o.birthday != null)
			birthday = o.getBirthday();
		if (url == null || url.length() == 0)
			url = o.url;
	}

	@Override
	public int compareTo(Contact o) {
		int k = 0;
		if (k == 0)
			k = givenname.compareTo(o.givenname);
		if (k == 0)
			k = surname.compareTo(o.surname);
		return k;
	}
}
