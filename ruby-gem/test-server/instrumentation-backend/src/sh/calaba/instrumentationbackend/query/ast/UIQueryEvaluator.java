package sh.calaba.instrumentationbackend.query.ast;

import java.util.ArrayList;
import java.util.List;

import sh.calaba.instrumentationbackend.actions.Operation;
import sh.calaba.instrumentationbackend.query.UIQueryResultVoid;
import sh.calaba.instrumentationbackend.query.ViewMapper;

public class UIQueryEvaluator {
	
	@SuppressWarnings({ "rawtypes" })
	public static List evaluateQueryWithOptions(List<UIQueryAST> query, List inputViews,
			List<Operation> operations) {
		
        long before = System.currentTimeMillis();
        
        List views = evaluateQueryForPath(query, inputViews);
        
        long after = System.currentTimeMillis();              
        String action = "EvaluateQuery";                               
        System.out.println(action+ " took: "+ (after-before) + "ms");
        
        before = System.currentTimeMillis();
        
        List result = applyOperations(views, operations);
        
        after = System.currentTimeMillis();              
        action = "ApplyOperations";                               
        System.out.println(action+ " took: "+ (after-before) + "ms");

        before = System.currentTimeMillis();
        
		List finalResult = mapViews(result);
        
		after = System.currentTimeMillis();              
        action = "MapViews";                               
        return finalResult;         	
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List mapViews(List result) {		
		List finalResult = new ArrayList(result.size());
		for (Object o : result) {
			finalResult.add(ViewMapper.mapView(o));
		}
		return finalResult;
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
			if (isDirection(step)) {
				currentDirection = directionFromAst(step);
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
	
	public static UIQueryDirection directionFromAst(UIQueryAST step) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean isDirection(UIQueryAST step) {
		// TODO Auto-generated method stub
		return false;
	}
			
}
