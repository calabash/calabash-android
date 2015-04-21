package sh.calaba.instrumentationbackend.query.ast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import org.antlr.runtime.tree.CommonTree;

import android.view.View;

public class UIQueryASTPredicate implements UIQueryAST {

	private final String propertyName;
	private final UIQueryASTPredicateRelation relation;
	private final Object valueToMatch;

	public UIQueryASTPredicate(String text,
			UIQueryASTPredicateRelation parsedRelation, Object parsedValue) {
		this.propertyName = text;
		this.relation = parsedRelation;
		this.valueToMatch = parsedValue;
	}

	@SuppressWarnings("rawtypes")
	@Override
    public List evaluateWithViews(final List inputViews,
                                  final UIQueryDirection direction, final UIQueryVisibility visibility) {

        List oldProcessing = new ArrayList();
        List result = new ArrayList();
        for (Object o : UIQueryUtils.uniq(inputViews)) {
            if (o instanceof View) {
                View view = (View) o;
                FutureTask<List> march = new FutureTask<List>(new MatchForViews(Arrays.asList(view), direction, visibility));
                UIQueryUtils.runOnViewThread(view, march);
                try {
                    result.addAll(march.get(10, TimeUnit.SECONDS));
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                oldProcessing.add(o);
            }
        }

        if (oldProcessing.size() > 0) {
            result.addAll((List) UIQueryUtils.evaluateSyncInMainThread(new MatchForViews(oldProcessing, direction, visibility)));
        }
        return result;
    }

    private class MatchForViews implements Callable<List> {

        private final List views;
        private final UIQueryDirection direction;
        private final UIQueryVisibility visibility;

        MatchForViews(List views, final UIQueryDirection direction, final UIQueryVisibility visibility) {
            this.views = views;
            this.direction = direction;
            this.visibility = visibility;
        }

        @Override
        public List call() throws Exception {
            List filteredResult = new ArrayList(16);
            for (int i = 0; i < views.size(); i++) {
                Object o = views.get(i);

                if (o instanceof Map) {
                    Map result = evaluateForMap((Map) o);
                    if (result != null) {
                        filteredResult.add(result);
                    }

                } else {
                    Object result = evaluateForObject(o, i);
                    if (result != null) {
                        filteredResult.add(result);
                    }
                }

            }

            return visibility.evaluateWithViews(filteredResult, direction, visibility);
        }
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
			} else {
				// let it fall through and check via general property access
				// in case the user actually wants to compre the real value of
				// getId()
			}
		}

		// there's no tag property for non Views, handle them all here
		if (this.propertyName.equals("tag")) {
			String tag = (o instanceof View ? UIQueryUtils.getTag((View) o) : null);
			if (this.relation.areRelated(tag, this.valueToMatch)) {
				return o;
			} else {
				return null;
			}
		}

		Method propertyAccessor = UIQueryUtils
				.hasProperty(o, this.propertyName);
		if (propertyAccessor == null) {
			return null;
		}
		Object value = UIQueryUtils.getProperty(o, propertyAccessor);

		if (this.relation.areRelated(value, this.valueToMatch)) {
			return o;
		} else if (this.valueToMatch instanceof String
                    && value != null
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

	private static UIQueryASTPredicateRelation parseRelation(CommonTree rel) {
		String relText = rel.getText().toUpperCase();
		boolean caseSensitive = true;
		final String CASE_INSENSITIVE_SPEC = "[C]";
		if (relText.endsWith(CASE_INSENSITIVE_SPEC)) {
			caseSensitive = false;
			relText = relText.substring(0,relText.length() - CASE_INSENSITIVE_SPEC.length());
		}

		if ("BEGINSWITH".equals(relText)) {
			return new BeginsWithRelation(caseSensitive);
		} else if ("ENDSWITH".equals(relText)) {
			return new EndsWithRelation(caseSensitive);
		} else if ("CONTAINS".equals(relText)) {
			return new ContainsRelation(caseSensitive);
		} else if ("LIKE".equals(relText)) {
			return new LikeRelation(caseSensitive);
		} else if ("<".equals(relText)) {
			return ComparisonOperator.LESSTHAN;
		} else if ("<=".equals(relText)) {
			return ComparisonOperator.LESSTHANOREQUAL;
		} else if ("=".equals(relText)) {
			return ComparisonOperator.EQUAL;
		} else if (">".equals(relText)) {
			return ComparisonOperator.GREATERTHAN;
		} else if (">=".equals(relText)) {
			return ComparisonOperator.GREATERTHANOREQUAL;
		} else if ("!=".equals(relText) || "<>".equals(relText)) {
			return ComparisonOperator.NOTEQUAL;
		} else {
			throw new IllegalStateException("Unsupported Relation: " + relText);
		}
	}

}
