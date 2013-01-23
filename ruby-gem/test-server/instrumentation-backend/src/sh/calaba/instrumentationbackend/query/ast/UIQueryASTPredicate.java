package sh.calaba.instrumentationbackend.query.ast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.antlr.runtime.tree.CommonTree;

import android.view.View;


public class UIQueryASTPredicate implements UIQueryAST {

	private final String propertyName;
	private final UIQueryASTPredicateRelation relation;
	private final Object valueToMatch;

	public UIQueryASTPredicate(String text, UIQueryASTPredicateRelation parsedRelation,
			Object parsedValue) {
		this.propertyName = text;
		this.relation = parsedRelation;
		this.valueToMatch = parsedValue;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List evaluateWithViews(final List inputViews, final UIQueryDirection direction,
			final UIQueryVisibility visibility) {
		return (List) UIQueryUtils.evaluateSyncInMainThread(new Callable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public Object call() throws Exception {
				List filteredResult = new ArrayList(16);
				for (int i = 0; i < inputViews.size(); i++) {
					Object o = inputViews.get(i);

					if (o instanceof Map) {
						Map result = evaluateForMap((Map) o);
						if (result != null) {
							filteredResult.add(result);
						}
						
					}
					else {
						Object result = evaluateForObject(o, i);
						if (result != null) {
							filteredResult.add(result);
						}
					}

				}

				return filteredResult;
			}
		});			
	}
	
	@SuppressWarnings("rawtypes")
	private Map evaluateForMap(Map map) {		
		if (map.containsKey(this.propertyName)) {											
			Object value = map.get(this.propertyName);
			if (this.relation.areRelated(value, this.valueToMatch)) {
				return map;
			}			
		} 
		return null;
	}

	private Object evaluateForObject(Object o, int index) {
		if (o instanceof View && this.propertyName.equals("id")) {
			View view = (View) o;
			String id = UIQueryUtils.getId(view);
			if (this.relation.areRelated(id, this.valueToMatch)) {
				return o;
			}
			else {
				//let it fall through and check via general property access 
				//in case the user actually wants to compre the real value of getId()
			}					
		} 	

		Method propertyAccessor = UIQueryUtils.hasProperty(o,
				this.propertyName);
		if (propertyAccessor == null) {
			return null;
		}
		Object value = UIQueryUtils.getProperty(o, propertyAccessor);

		if (this.relation.areRelated(value, this.valueToMatch)) {
			return o;
		} else if (this.valueToMatch instanceof String
				&& this.relation.areRelated(value.toString(), this.valueToMatch)) {
			return o;
		} else {
			return null;	
		}		
	}
	
	public static UIQueryASTPredicate newPredicateFromAST(CommonTree step) {
		// TODO Auto-generated method stub
		if (step.getChildCount() != 3) {
			throw new IllegalStateException("Bad Predicate query: "+step+". Expected form {getter RELATION value}.");
		}
		CommonTree prop = (CommonTree) step.getChild(0);
		CommonTree rel = (CommonTree) step.getChild(1);
		CommonTree val = (CommonTree) step.getChild(2);
			

		return new UIQueryASTPredicate(prop.getText(),
				UIQueryASTPredicate.parseRelation(rel),
				UIQueryUtils.parseValue(val));
	}

	//('BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE')('[c]')?
	private static UIQueryASTPredicateRelation parseRelation(CommonTree rel) {
		String relText = rel.getText();
		if ("BEGINSWITH".equals(relText)) {
			return new BeginsWithRelation(true);
		}
		else if ("ENDSWITH".equals(relText)) {
			return new EndsWithRelation(true);
		}
		else if ("CONTAINS".equals(relText)) {
			return new ContainsRelation(true);
		}
		else if ("LIKE".equals(relText)) {
			return new LikeRelation(true);			
		}
		else {
			throw new IllegalStateException("Unsupported Relation: "+relText);
		}
	}

}
