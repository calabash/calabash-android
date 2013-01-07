package sh.calaba.instrumentationbackend.query.ast;

import java.util.List;

import android.os.ConditionVariable;

public interface UIQueryAST {
	@SuppressWarnings("rawtypes")
	public List evaluateWithViews(List inputViews, UIQueryDirection direction, UIQueryVisibility visibility, ConditionVariable computationFinished);
}
