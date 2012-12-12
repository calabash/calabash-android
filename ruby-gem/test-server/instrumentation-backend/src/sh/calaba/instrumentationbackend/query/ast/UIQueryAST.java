package sh.calaba.instrumentationbackend.query.ast;

import java.util.List;

public interface UIQueryAST {
	@SuppressWarnings("rawtypes")
	public List evaluateWithViewsAndDirection(List inputViews, UIQueryDirection direction);
}
