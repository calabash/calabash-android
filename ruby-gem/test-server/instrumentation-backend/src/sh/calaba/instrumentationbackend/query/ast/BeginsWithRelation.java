package sh.calaba.instrumentationbackend.query.ast;

import android.annotation.SuppressLint;

public class BeginsWithRelation implements UIQueryASTPredicateRelation {

	private final boolean caseSensitive;
		
	public BeginsWithRelation(boolean isCaseSensitive) {
		super();
		this.caseSensitive = isCaseSensitive;
	}

	@Override
	public boolean isCaseSensitive() { 
		return caseSensitive;
	}

	/**
	 * Does firstValue BEGIN WITH secondValue?
	 */
	@SuppressLint("DefaultLocale")
	@Override
	public boolean areRelated(Object firstValue, Object secondValue) {
		if (firstValue == secondValue) { 
			return true;
		}
		if (firstValue == null || secondValue == null) {
			return false;
		}
		if (firstValue instanceof String && secondValue instanceof String) {
			String firstStr = (String) firstValue;
			String secondStr = (String) secondValue;
			if (!this.caseSensitive) {
				firstStr = firstStr.toLowerCase();
				secondStr = secondStr.toLowerCase();
			}			
			return firstStr.startsWith(secondStr);			
		}
		else {
			return false;
		}
	}

}
