package org.libriami.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class PushBackLineReader {

	private BufferedReader in;

	private String pushedBackLine;

	private boolean isEatEmptyLines;

	public PushBackLineReader(BufferedReader in, boolean isEatEmptyLines) {
		this.in = in;
		this.isEatEmptyLines = isEatEmptyLines;
	}

	public String readLine() throws IOException {
		if (pushedBackLine != null) {
			String tmp = pushedBackLine;
			pushedBackLine = null;
			return tmp;
		}

		String line = innerReadLine();
		if (line == null)
			return null;

		if (line.length() > 0 && line.charAt(0) == '#') {
			line = in.readLine();
			// FIXME: Eat more than one comment line!
		}

		return line;
	}

	/**
	 * Read next line and ignore commented lines starting with # character
	 * 
	 * @return Next line or null if stream is closed
	 * @throws IOException
	 *             In case of IO errors
	 */
	private String innerReadLine() throws IOException {
		String line = null;

		// Commen lines starting with #
		boolean isCommented = false;
		// Empty lines ignored too (more failsaife)
		boolean isEmpty = false;
		do {
			line = in.readLine();

			isEmpty = isEatEmptyLines && (line != null && line.length() == 0);

			// Check for comment line and eat them
			isCommented = line != null && line.length() > 0 && line.charAt(0) == '#';

		} while (isCommented || isEmpty);

		return line;
	}

	public void pushBack(String line) throws IOException {
		if (pushedBackLine == null) {
			pushedBackLine = line;
		} else {
			throw new IOException("Pushback not yet consumed. Couldnt store more than one line");
		}
	}

	public void close() throws IOException {
		in.close();
	}

}
