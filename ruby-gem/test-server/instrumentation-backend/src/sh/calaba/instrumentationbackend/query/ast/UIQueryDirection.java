package sh.calaba.instrumentationbackend.query.ast;

import java.util.List;

public enum UIQueryDirection implements UIQueryAST {
	DESCENDANT, CHILD, PARENT, SIBLING;

	@SuppressWarnings("rawtypes")
	@Override
	public List evaluateWithViews(List inputViews, UIQueryDirection direction,
			UIQueryVisibility visibility) {
		//Never called. Not so pretty, I know.
		throw new UnsupportedOperationException("This method should never be called");
	}
}
