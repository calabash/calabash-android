package sh.calaba.instrumentationbackend.query.ast;

import java.util.ArrayList;
import java.util.List;

import android.os.ConditionVariable;

public enum UIQueryVisibility implements UIQueryAST {
	ALL {		
		@Override
		@SuppressWarnings("rawtypes")
		public List evaluateWithViews(List inputViews,
				UIQueryDirection direction, UIQueryVisibility self, ConditionVariable computationFinished) {			
			return inputViews;
		} 
	},
	
	
	VISIBLE {
		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public List evaluateWithViews(List inputViews,
				UIQueryDirection direction, UIQueryVisibility self, ConditionVariable computationFinished) {
			List filtered = new ArrayList(inputViews.size());
			for (Object o : inputViews) {
				if (UIQueryUtils.isVisible(o)) {
				    filtered.add(o);					
				}				
			}			
			return filtered;
		} 
	};
	
}
