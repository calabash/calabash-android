package sh.calaba.instrumentationbackend.query.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import sh.calaba.instrumentationbackend.query.antlr.UIQueryLexer;
import sh.calaba.instrumentationbackend.query.antlr.UIQueryParser;

public class UIQueryEvaluator {
	
	@SuppressWarnings({ "rawtypes"})
	public static List evaluateQuery(String query, List inputViews) 
	{						
		return evaluateQueryForPath(parseQuery(query), inputViews);			
	}
	
	@SuppressWarnings("unchecked")
	public static List<UIQueryAST> parseQuery(String query)
	{
		UIQueryLexer lexer = new UIQueryLexer(new ANTLRStringStream(query));
		UIQueryParser parser = new UIQueryParser(new CommonTokenStream(lexer));
		
		UIQueryParser.query_return q;
		try {
			q = parser.query();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			throw new InvalidUIQueryException(e.getMessage());
		}
		if (q == null)
		{
			throw new InvalidUIQueryException(query); 
		}
		CommonTree rootNode = (CommonTree) q.getTree();
		List<CommonTree> queryPath = rootNode.getChildren();
		
		if (queryPath == null || queryPath.isEmpty())
		{
			queryPath = Collections.singletonList(rootNode);
		}
		
		return mapUIQueryFromAstNodes(queryPath);
	}

	@SuppressWarnings("rawtypes")
	private static List evaluateQueryForPath(List<UIQueryAST> queryPath,
			List inputViews) {
		
		List currentResult = inputViews;
		UIQueryDirection currentDirection = UIQueryDirection.DESCENDANT;
		for (UIQueryAST step : queryPath)
		{
			if (isDirection(step))
			{
				currentDirection = directionFromAst(step);
			}
			else
			{
				currentResult = step.evaluateWithViewsAndDirection(currentResult, currentDirection);	
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

	public static List<UIQueryAST> mapUIQueryFromAstNodes(List<CommonTree> nodes)
	{
		List<UIQueryAST> mapped = new ArrayList<UIQueryAST>(nodes.size());
		for (CommonTree t : nodes) {mapped.add(uiQueryFromAst(t));}
		return mapped;
	}
	public static UIQueryAST uiQueryFromAst(CommonTree step) 
	{
		String stepType = UIQueryParser.tokenNames[step.getType()];
		switch (step.getType())
		{
			case UIQueryParser.QUALIFIED_NAME:
			try {
				return new UIQueryASTClassName(Class.forName(step.getText()));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				throw new InvalidUIQueryException("Qualified class name: "+step.getText()+" not found. ("+e.getMessage()+")");
			}
			case UIQueryParser.NAME:
				return new UIQueryASTClassName(step.getText());
				
			case UIQueryParser.FILTER_COLON:				
				return UIQueryASTFilter.fromAST(step);
			default:
				throw new InvalidUIQueryException("Unknown query: "+stepType + " with text: "+step.getText());
				
		}				
		
	}

}
