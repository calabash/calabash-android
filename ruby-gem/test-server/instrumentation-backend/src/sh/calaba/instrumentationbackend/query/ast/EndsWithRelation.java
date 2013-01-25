package sh.calaba.instrumentationbackend.query.ast;

public class EndsWithRelation implements UIQueryASTPredicateRelation {

	private final boolean caseSensitive;
	
	public EndsWithRelation(boolean isCaseSensitive) {
		super();
		this.caseSensitive = isCaseSensitive;
	}

	public boolean isCaseSensitive() { 
		return caseSensitive;
	}


	/**
	 * Does firstValue END WITH secondValue?
	 */
	@Override
	public boolean areRelated(Object firstValue, Object secondValue) {
		if (firstValue == secondValue) { 
			return true;
		}
		if (firstValue == null || secondValue == null) {
			return false;
		}
		if (firstValue instanceof CharSequence && secondValue instanceof CharSequence) {
			String firstStr = firstValue.toString();
			String secondStr = secondValue.toString();
			if (!isCaseSensitive()) {
				firstStr = firstStr.toLowerCase();
				secondStr = secondStr.toLowerCase();
			}						
			return firstStr.endsWith(secondStr);		
		}
		else {
			return false;
		}
	}

}
