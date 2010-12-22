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

import org.libriami.model.Contact;

/**
 * Passes all warnings and errors to STDERR.
 */
public class ConsoleCoderListener implements CoderListener {

	@Override
	public void warn(Contact c, String message) {
		System.err.println("WARN: " + message);
	}

	@Override
	public void error(Contact c, String message) {
		System.err.println("ERROR: " + message);
	}

}
