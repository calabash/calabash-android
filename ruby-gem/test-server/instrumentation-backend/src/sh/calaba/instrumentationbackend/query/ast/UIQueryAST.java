package sh.calaba.instrumentationbackend.query.ast;

import java.util.List;

public interface UIQueryAST {
	@SuppressWarnings("rawtypes")
	public List evaluateWithViews(List inputViews, UIQueryDirection direction, UIQueryVisibility visibility);
}
