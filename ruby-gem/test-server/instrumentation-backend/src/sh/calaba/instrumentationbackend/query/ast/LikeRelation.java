package sh.calaba.instrumentationbackend.query.ast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LikeRelation implements UIQueryASTPredicateRelation {

	private final boolean caseSensitive;

	public LikeRelation(boolean isCaseSensitive) {
		super();
		this.caseSensitive = isCaseSensitive;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * Is firstValue LIKE secondValue? In this case secondValue is a pattern ala
	 * 'Cou* Down' which will match a string 'Count Down'.
	 */
	@Override
	public boolean areRelated(Object firstValue, Object secondValue) {
		if (firstValue == secondValue) {
			return true;
		}
		if (firstValue == null || secondValue == null) {
			return false;
		}
		if (firstValue instanceof CharSequence
				&& secondValue instanceof CharSequence) {
			String firstStr = firstValue.toString();
			String patternStr = secondValue.toString();
			Matcher matcher = matcherFrom(patternStr, firstStr);
			return matcher.matches();
		} else {
			return false;
		}
	}

	private Matcher matcherFrom(String patternStr, String stringToMatch) {
		Pattern compiledPat;
		if (isCaseSensitive()) {
			compiledPat = Pattern.compile(translatePatternString(patternStr));
		} else {
			compiledPat = Pattern.compile(translatePatternString(patternStr),
					Pattern.CASE_INSENSITIVE);
		}
		return compiledPat.matcher(stringToMatch);
	}

	private String translatePatternString(String patternStr) {

		StringBuilder converted = new StringBuilder();
		int beginAt = 0;
		for (int i = 0; i < patternStr.length(); i++) {
			char charAti = patternStr.charAt(i);
			if (charAti == '*' || charAti == '%') {
				String subString = patternStr.substring(beginAt, i);
				beginAt = i + 1;
				converted.append(Pattern.quote(subString));
				if (charAti == '*') {
					converted.append(".*");
				} else if (charAti == '%') {
					converted.append(".");
				}
			}
		}
		if (beginAt < patternStr.length()) {
			String subString = patternStr.substring(beginAt,
					patternStr.length());
			converted.append(Pattern.quote(subString));
		}

		return "^" + converted.toString() + "$";
	}

}