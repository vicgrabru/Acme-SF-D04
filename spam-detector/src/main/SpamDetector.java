
package spamDetector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpamDetector {

	/*
	 * private static List<String> spamList = List.of("([,\\s]+)?sex([,\\s]+)", "([,\\s]+)?viagra([,\\s]+)", "([,\\s]+)?cialis([,\\s]+)", "([,\\s]+)?one\\s+million([,\\s]+)", "([,\\s]+)?you\\s+won([,\\s]+)", "([,\\s]+)?nigeria([,\\s]+)",
	 * "([,\\s]+)?sexo([,\\s]+)", "([,\\s]+)?un\\s+millon([,\\s]+)", "([,\\s]+)?has\\s+ganado([,\\s]+)", "([,\\s]+)?sex$", "([,\\s]+)?viagra$", "([,\\s]+)?cialis$", "([,\\s]+)?one\\s+million$", "([,\\s]+)?you\\s+won$", "([,\\s]+)?nigeria$",
	 * "([,\\s]+)?sexo$", "([,\\s]+)?un\\s+millon$", "([,\\s]+)?has\\s+ganado$");
	 */

	public static boolean checkTextValue(String text, final List<String> spamWords, double threshold) {
		Set<String> translatedSpamWords = new HashSet<>();
		for (String spam : spamWords)
			SpamDetector.addSpam(spam, translatedSpamWords);
		text = text.toLowerCase();
		int spamPoints = 0;
		for (String spam : translatedSpamWords) {
			Pattern pattern = Pattern.compile(spam);
			Matcher matcher = pattern.matcher(text);
			StringBuffer result = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(result, "");
				spamPoints++;
			}
			if (!result.toString().isEmpty())
				text = result.toString();
		}
		if (threshold < 0)
			threshold = 0;
		else if (threshold > 1)
			threshold = 1;
		return threshold < (double) spamPoints / (text.split(" ").length + spamPoints);
	}
	private static void addSpam(final String text, final Set<String> spamSet) {
		if (text == null)
			return;
		String replacedSpam = text.toLowerCase().replace(" ", "\\s+");
		spamSet.add("([,\\s]+)?" + replacedSpam + "([,\\s]+)");
		spamSet.add("([,\\s]+)?" + replacedSpam + "$");
	}
	/*
	 * public static void deleteSpam(final String text) {
	 * if (text == null)
	 * return;
	 * String replacedSpam = text.toLowerCase().replace(" ", "\\s+");
	 * SpamDetector.spamList.remove("([,\\s]+)?" + replacedSpam + "([,\\s]+)");
	 * SpamDetector.spamList.remove("([,\\s]+)?" + replacedSpam + "$");
	 * }
	 */
}
