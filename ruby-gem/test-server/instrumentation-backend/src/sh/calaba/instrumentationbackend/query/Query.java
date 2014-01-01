package sh.calaba.instrumentationbackend.query;

import static sh.calaba.instrumentationbackend.InstrumentationBackend.viewFetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import sh.calaba.instrumentationbackend.query.antlr.UIQueryLexer;
import sh.calaba.instrumentationbackend.query.antlr.UIQueryParser;
import sh.calaba.instrumentationbackend.query.ast.InvalidUIQueryException;
import sh.calaba.instrumentationbackend.query.ast.UIQueryAST;
import sh.calaba.instrumentationbackend.query.ast.UIQueryASTClassName;
import sh.calaba.instrumentationbackend.query.ast.UIQueryASTPredicate;
import sh.calaba.instrumentationbackend.query.ast.UIQueryASTWith;
import sh.calaba.instrumentationbackend.query.ast.UIQueryDirection;
import sh.calaba.instrumentationbackend.query.ast.UIQueryEvaluator;
import sh.calaba.instrumentationbackend.query.ast.UIQueryVisibility;
import android.view.View;

public class Query {

	private String queryString;
	@SuppressWarnings("rawtypes")
	private List operations;

	public Query(String queryString) {
		this.queryString = queryString;
		this.operations = Collections.EMPTY_LIST;
		if (this.queryString == null || this.queryString.trim().equals("")) {
			throw new IllegalArgumentException("Illegal query: "
					+ this.queryString);
		}
	}

	@SuppressWarnings("rawtypes")
	public Query(String queryString, List args) {
		this(queryString);
		this.operations = args;
	}

	public QueryResult executeQuery() {
		return UIQueryEvaluator.evaluateQueryWithOptions(parseQuery(this.queryString), rootViews(), parseOperations(this.operations));		
	}

	@SuppressWarnings("rawtypes")
	public static List<Operation> parseOperations(List ops) {
		List<Operation> result = new ArrayList<Operation>(ops.size());
		for (Object o : ops) {
			Operation op = null;
			if (o instanceof Operation) {
				op = (Operation) o;												
			}
			else if (o instanceof String) {
				op = new PropertyOperation((String) o);	
			}
			else if (o instanceof Map) {
				Map mapOp = (Map) o;				
				String methodName = (String) mapOp.get("method_name");
				if (methodName == null) {
					throw new IllegalArgumentException("Trying to convert a Map without method_name to an operation. " + mapOp.toString());
				}
				List arguments = (List) mapOp.get("arguments");
				if (arguments == null) {
					throw new IllegalArgumentException("Trying to convert a Map without arguments to an operation. " + mapOp.toString());
				}
				op = new InvocationOperation(methodName, arguments);
			}
			result.add(op);								
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static List<UIQueryAST> parseQuery(String query) {
		UIQueryLexer lexer = new UIQueryLexer(new ANTLRStringStream(query));
		UIQueryParser parser = new UIQueryParser(new CommonTokenStream(lexer));

		UIQueryParser.query_return q;
		try {
			q = parser.query();
		} catch (RecognitionException e) {
			throw new InvalidUIQueryException(e.getMessage());
		}
		if (q == null) {
			throw new InvalidUIQueryException(query);
		}
		CommonTree rootNode = (CommonTree) q.getTree();
		List<CommonTree> queryPath = rootNode.getChildren();

		if (queryPath == null || queryPath.isEmpty()) {
			queryPath = Collections.singletonList(rootNode);
		}

		return mapUIQueryFromAstNodes(queryPath);
	}


	public static List<UIQueryAST> mapUIQueryFromAstNodes(List<CommonTree> nodes) {
		List<UIQueryAST> mapped = new ArrayList<UIQueryAST>(nodes.size());
		for (CommonTree t : nodes) {
			mapped.add(uiQueryFromAst(t));
		}
		return mapped;
	}

	public static UIQueryAST uiQueryFromAst(CommonTree step) {
		String stepType = UIQueryParser.tokenNames[step.getType()];
		switch (step.getType()) {
		case UIQueryParser.QUALIFIED_NAME:
			try {
				return new UIQueryASTClassName(Class.forName(step.getText()));
			} catch (ClassNotFoundException e) {
				return new UIQueryASTClassName((String)null);
			}
		case UIQueryParser.NAME:
			return new UIQueryASTClassName(step.getText());
		
		case UIQueryParser.WILDCARD:
			try {
				return new UIQueryASTClassName(Class.forName("android.view.View"));
			} catch (ClassNotFoundException e) {
				//Cannot happen
				throw new IllegalStateException(e);
			}
			

			
		case UIQueryParser.FILTER_COLON:
			return UIQueryASTWith.fromAST(step);
			
		case UIQueryParser.ALL:
			return UIQueryVisibility.ALL;	
			
		case UIQueryParser.VISIBLE:
			return UIQueryVisibility.VISIBLE;
			
		case UIQueryParser.BEGINPRED:
			return UIQueryASTPredicate.newPredicateFromAST(step);
		case UIQueryParser.DIRECTION:
			return UIQueryDirection.valueOf(step.getText().toUpperCase());			
			
		default:
			throw new InvalidUIQueryException("Unknown query: " + stepType
					+ " with text: " + step.getText());

		}

	}

    public List<View> rootViews() {
        Set<View> parents = new HashSet<View>();
        for (View v : viewFetcher.getAllViews(false))
        {
            View parent = viewFetcher.getTopParent(v);            
            parents.add(parent);
        }
        List<View> results = new ArrayList<View>();
        results.addAll(parents);
        return results;
    }


}
