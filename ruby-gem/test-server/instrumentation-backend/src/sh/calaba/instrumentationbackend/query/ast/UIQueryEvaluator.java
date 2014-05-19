package sh.calaba.instrumentationbackend.query.ast;

import java.util.ArrayList;
import java.util.List;

import sh.calaba.instrumentationbackend.query.Operation;
import sh.calaba.instrumentationbackend.query.QueryResult;
import sh.calaba.instrumentationbackend.query.UIQueryResultVoid;
import sh.calaba.instrumentationbackend.query.ViewMapper;

public class UIQueryEvaluator {
	
	@SuppressWarnings({ "rawtypes" })
	public static QueryResult evaluateQueryWithOptions(List<UIQueryAST> query, List inputViews, List<Operation> operations) {
        List views = evaluateQueryForPath(query, inputViews);
        List result = applyOperations(views, operations);
        return new QueryResult(result);
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List applyOperations(List views, List<Operation> operations) {
		List result = views;
		for(Operation op : operations) {
			List nextResult = new ArrayList(result.size());
			for (Object obj : result) {
				try {
					nextResult.add(op.apply(obj));	
				} catch (Exception e) {
					e.printStackTrace();
					nextResult.add(UIQueryResultVoid.instance.asMap(op.getName(), obj, e.getMessage()));
				}				
			}
			result = nextResult;
		}
		return result;
	}


	@SuppressWarnings("rawtypes")
	private static List evaluateQueryForPath(List<UIQueryAST> queryPath,
			List inputViews) {

		List currentResult = inputViews;
		UIQueryDirection currentDirection = UIQueryDirection.DESCENDANT;
		UIQueryVisibility currentVisibility = UIQueryVisibility.VISIBLE;
		
		for (UIQueryAST step : queryPath) {			
			if (step instanceof UIQueryDirection) {
				currentDirection = (UIQueryDirection) step;
			}
			else if (step instanceof UIQueryVisibility) {
				currentVisibility = (UIQueryVisibility) step;
			}
			else {
				currentResult = step.evaluateWithViews(currentResult, currentDirection,currentVisibility);
			}

		}
		return currentResult;
	}
		

	public static boolean isDirection(UIQueryAST step) {
		return step instanceof UIQueryDirection;
	}
			
}
