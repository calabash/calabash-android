package sh.calaba.instrumentationbackend.query.tests;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import sh.calaba.instrumentationbackend.query.ast.UIQueryAST;
import sh.calaba.instrumentationbackend.query.ast.UIQueryASTClassName;
import sh.calaba.instrumentationbackend.query.ast.UIQueryEvaluator;

public class UIQueryTest extends TestCase {

	@SuppressWarnings("rawtypes")
	public void testEvaluateQueryOnEmpty() {
		String query = "button";
		List res = UIQueryEvaluator.evaluateQueryWithOptions(query, Collections.EMPTY_LIST, null, null);		
		assertEquals(0, res.size());
	}

	public void testConvertSimpleQueryToUIQueryAst() throws Exception {
		List<UIQueryAST> parsedQuery = UIQueryEvaluator.parseQuery("button");
		assertEquals(1, parsedQuery.size());
		
		UIQueryAST uiQueryAST = parsedQuery.get(0);
		
		assertTrue(uiQueryAST instanceof UIQueryASTClassName);
		assertEquals("button", ((UIQueryASTClassName)uiQueryAST).simpleClassName);
		
	}

	public void testConvertQualifiedQueryToUIQueryAst() throws Exception {
		List<UIQueryAST> parsedQuery = UIQueryEvaluator.parseQuery("junit.framework.TestCase");
		assertEquals(1, parsedQuery.size());
		
		UIQueryAST uiQueryAST = parsedQuery.get(0);
		
		assertTrue(uiQueryAST instanceof UIQueryASTClassName);				
		assertEquals(junit.framework.TestCase.class, ((UIQueryASTClassName)uiQueryAST).qualifiedClassName);
	}
	
	public void testConvertComposedQueryToUIQueryAst() throws Exception {
		List<UIQueryAST> parsedQuery = UIQueryEvaluator.parseQuery("view button");
		assertEquals(2, parsedQuery.size());
		
		UIQueryAST uiQueryASTFirst = parsedQuery.get(0);
		UIQueryAST uiQueryASTSecond = parsedQuery.get(1);
		
		assertTrue(uiQueryASTFirst instanceof UIQueryASTClassName);
		assertTrue(uiQueryASTSecond instanceof UIQueryASTClassName);
		
		assertEquals("view", ((UIQueryASTClassName)uiQueryASTFirst).simpleClassName);
		assertEquals("button", ((UIQueryASTClassName)uiQueryASTSecond).simpleClassName);
		
	}

	
	private static class View {
		@SuppressWarnings("rawtypes")
		List views;

		@SuppressWarnings("rawtypes")
		public View(List views) {
			super();
			this.views = views;
		}
		public Object getChildAt(int idx)
		{			
			return this.views.get(idx);
		}
		@SuppressWarnings("unused")
		public Object getChildCount() {return this.views.size();}
		
	}
	private static class SubView extends View {

		@SuppressWarnings("rawtypes")
		public SubView(List views) {
			super(views);
		}
		
	}
	private static class Button extends View {

		@SuppressWarnings("rawtypes")
		public Button(List views) {
			super(views);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public void testEvaluateSimpleQuery() throws Exception {
		List<View> views = setupViews();
		
		List evaluateQuery = UIQueryEvaluator.evaluateQueryWithOptions("subview", views, null, null);
		assertEquals(1, evaluateQuery.size());
		assertTrue(evaluateQuery.get(0) instanceof SubView);
		
		List evaluateQueryButtons = UIQueryEvaluator.evaluateQueryWithOptions("button", views, null, null);
		assertEquals(3, evaluateQueryButtons.size());
		assertTrue(evaluateQueryButtons.get(0) instanceof Button);
		assertTrue(evaluateQueryButtons.get(1) instanceof Button);
		assertTrue(evaluateQueryButtons.get(2) instanceof Button);
		
	}
	

	@SuppressWarnings("rawtypes")
	public void testEvaluateComposedQuery() throws Exception {
		List<View> views = setupViews();
		SubView vv = (SubView) views.get(1);
		Button b = (Button) vv.getChildAt(0);
		
		List evaluateQuery = UIQueryEvaluator.evaluateQueryWithOptions("subview button", views, null, null);
		assertEquals(1, evaluateQuery.size());
		assertTrue(evaluateQuery.get(0) instanceof Button);
		assertEquals(evaluateQuery.get(0), b);
		
		
	}

	private List<View> setupViews() {
		return Arrays.asList(new View[]{
			new Button(Collections.EMPTY_LIST),
			new SubView(Arrays.asList(new View[]{
					new Button(Collections.EMPTY_LIST)
			})),
			new Button(Collections.EMPTY_LIST)
		});
	}

}
