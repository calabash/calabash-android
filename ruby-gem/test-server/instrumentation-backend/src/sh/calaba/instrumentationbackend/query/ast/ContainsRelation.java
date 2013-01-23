package sh.calaba.instrumentationbackend.query.ast;

public class ContainsRelation implements UIQueryASTPredicateRelation {

	private final boolean caseSensitive;
	
	public ContainsRelation(boolean isCaseSensitive) {
		super();
		this.caseSensitive = isCaseSensitive;
	}

	@Override
	public boolean isCaseSensitive() { 
		return caseSensitive;
	}

	/**
	 * Does firstValue CONTAIN secondValue?
	 */
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
			return firstStr.indexOf(secondStr) != -1;	
		}
		else {
			return false;
		}
	}

}
