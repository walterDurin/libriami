package org.libriami.utils;

public class StringEncoder {

	private static final int ENCODING_MAX_LINE_WIDTH = 80;
	private static String indent = " ";

	public static void encode(String s, StringBuilder sb) {

		// sb.append(s);

		// Encode 1st line
		int i = 0;
		int max = Math.min(ENCODING_MAX_LINE_WIDTH, s.length());
		sb.append(s.substring(i, max));
		i += max;

		// Remaining lines will have an indent and need to get calculated
		// differently
		int schritt = ENCODING_MAX_LINE_WIDTH - indent.length();
		while (i < s.length()) {
			max = Math.min(i + schritt, s.length());
			sb.append("\n");
			sb.append(indent);
			sb.append(s.substring(i, max));
			i = max;
		}
	}
}
