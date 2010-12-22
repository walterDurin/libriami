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

package org.libriami.model.tools.compare;

/**
 * Interface which is used to compare objects and merge parameters of the same.
 */
public interface ApproxMatchable {

	/**
	 * Returns a value between 0 and 1 to indicate how much similar this instance is, compared to the given one. 0 means different. 1 means
	 * equal.
	 * 
	 * @param other Other instance to compare with
	 * @return Value between 0..1 as an indicator how equal both instances are
	 */
	double matchTo(ApproxMatchable other);

	// FIXME: Really both methods part of this interface?

	/**
	 * Merges parameters of the other instance into this one.
	 * 
	 * @param other Other instance to merge parameters from
	 */
	void merge(ApproxMatchable other);

}
