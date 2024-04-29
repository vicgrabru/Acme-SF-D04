package spamDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpamDetector {

	private static List<String>	spamList	= List.of("([,\\s]+)?sex([,\\s]+)", "([,\\s]+)?viagra([,\\s]+)", "([,\\s]+)?cialis([,\\s]+)", "([,\\s]+)?one\\s+million([,\\s]+)", "([,\\s]+)?you\\s+won([,\\s]+)", "([,\\s]+)?nigeria([,\\s]+)",
		"([,\\s]+)?sexo([,\\s]+)", "([,\\s]+)?un\\s+millon([,\\s]+)", "([,\\s]+)?has\\s+ganado([,\\s]+)", "([,\\s]+)?sex$", "([,\\s]+)?viagra$", "([,\\s]+)?cialis$", "([,\\s]+)?one\\s+million$", "([,\\s]+)?you\\s+won$", "([,\\s]+)?nigeria$",
		"([,\\s]+)?sexo$", "([,\\s]+)?un\\s+millon$", "([,\\s]+)?has\\s+ganado$");
	private static int			threshold	= 10;


	public static boolean checkTextValue(String text) {
		List<String> spamList = new ArrayList<>(SpamDetector.spamList);
		text = text.toLowerCase();
		int spamPoints = 0;
		for (String spam : spamList) {
			Pattern pattern = Pattern.compile(spam);
			Matcher matcher = pattern.matcher(text);
			StringBuffer result = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(result, "");
				spamPoints++;
			}
			if (!result.toString().isEmpty())
				text = result.toString();
			StringBuffer result = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(result, "");
				spamPoints++;
			}
			if (!result.toString().isEmpty())
				text = result.toString();
		}
		return SpamDetector.threshold < (double) spamPoints / (text.split(" ").length + spamPoints) * 100;
		return SpamDetector.threshold < (double) spamPoints / (text.split(" ").length + spamPoints) * 100;
	}
	public static void setSpamList(final List<String> spamList) {
		if (spamList == null)
			return;
		SpamDetector.spamList = new ArrayList<>();
		for (int i = 0; i < spamList.size(); i++)
			SpamDetector.addSpam(spamList.get(i));
	}

	public static List<String> getSpamList() {
		return new ArrayList<>(SpamDetector.spamList.stream().map(spam -> {
			String replacedSpam = spam.toLowerCase().replace("\\s+", " ");
			replacedSpam = spam.toLowerCase().replace("[,\\s]+", " ");
			replacedSpam = spam.toLowerCase().replace("([,\\s]+)?", " ");
			if (replacedSpam.endsWith("$"))
				replacedSpam = replacedSpam.substring(0, replacedSpam.length());
			return replacedSpam;
		}).collect(Collectors.toSet()));
	}

	public static int getThreshold() {
		return SpamDetector.threshold;
	}
	public static void addSpam(final String text) {
		if (text == null)
			return;
		String replacedSpam = text.toLowerCase().replace(" ", "\\s+");
		SpamDetector.spamList.add("([,\\s]+)?" + replacedSpam + "([,\\s]+)");
		SpamDetector.spamList.add("([,\\s]+)?" + replacedSpam + "$");
	}
	public static void deleteSpam(final String text) {
		if (text == null)
			return;
		String replacedSpam = text.toLowerCase().replace(" ", "\\s+");
		SpamDetector.spamList.remove("([,\\s]+)?" + replacedSpam + "([,\\s]+)");
		SpamDetector.spamList.remove("([,\\s]+)?" + replacedSpam + "$");
	}
	public static void setThreshold(final int threshold) {
		if (threshold < 0 || threshold > 100)
			return;
		SpamDetector.threshold = threshold;
	}
}
