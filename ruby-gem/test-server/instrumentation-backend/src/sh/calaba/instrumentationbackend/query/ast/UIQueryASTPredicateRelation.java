package sh.calaba.instrumentationbackend.query.ast;

public interface UIQueryASTPredicateRelation {			
	public boolean isCaseSensitive();
	public boolean areRelated(Object firstValue, Object secondValue);
}
