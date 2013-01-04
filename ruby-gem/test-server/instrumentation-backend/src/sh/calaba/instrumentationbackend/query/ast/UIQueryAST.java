package sh.calaba.instrumentationbackend.query.ast;

import java.util.List;

import android.os.ConditionVariable;

public interface UIQueryAST {
	@SuppressWarnings("rawtypes")
	public List evaluateWithViewsAndDirection(List inputViews, UIQueryDirection direction, ConditionVariable computationFinished);
}
