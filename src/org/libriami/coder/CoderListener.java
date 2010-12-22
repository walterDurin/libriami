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
 * Code Listener notifies if an encoding or decoding warning or error occurs. This could be displayed to the user or handled by a tool
 * itself.
 */
public interface CoderListener {

	/**
	 * Warning about encoding/decoding problem of given contact. The message describes the problem in more detail.
	 * 
	 * @param c Contact where problem exist
	 * @param message Message with more detailed information about the problem
	 */
	void warn(Contact c, String message);

	/**
	 * Error about encoding/decoding problem of given contact. The message describes the problem in more detail.
	 * 
	 * @param c Contact where problem exist
	 * @param message Message with more detailed information about the problem
	 */
	void error(Contact c, String message);

}
