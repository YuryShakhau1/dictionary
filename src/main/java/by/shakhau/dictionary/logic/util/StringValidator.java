package by.shakhau.dictionary.logic.util;

public class StringValidator {

	public static boolean validString(String sentence) {
		int letterCount = 0;
		for (int i = 0; i < sentence.length(); i++) {
			char c = sentence.charAt(i);
			if ((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= 'А' && c <= 'я')) {
				letterCount++;
			}
		}
		return ((double) letterCount / sentence.length() > 0.5D);
	}
}
