package sh.calaba.instrumentationbackend.query.ast;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class UIQueryASTClassName implements UIQueryAST {
	public final String simpleClassName;	
	@SuppressWarnings("rawtypes")
	public final Class qualifiedClassName;
	
	public UIQueryASTClassName(String simpleClassName) 
	{		
		this.simpleClassName = simpleClassName;
		this.qualifiedClassName = null;
	}
	
	@SuppressWarnings("rawtypes")
	public UIQueryASTClassName(Class qualifiedClassName)
	{
		if (qualifiedClassName == null) {throw new IllegalArgumentException("Cannot instantiate with null class");}
		this.qualifiedClassName = qualifiedClassName;
		this.simpleClassName = null;		
	}

	@SuppressWarnings({ "rawtypes"})
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

        public List call() throws Exception {
            List result = new ArrayList(8);
            for (Object o : views)
            {
                switch(direction) {
                    case DESCENDANT:
                        addDecendantMatchesToResult(o,result);
                        break;
                    case CHILD:
                        addChildMatchesToResult(o,result);
                        break;
                    case PARENT:
                        addParentMatchesToResult(o,result);
                        break;
                    case SIBLING:
                        addSiblingMatchesToResult(o,result);
                        break;
                }
            }

            List filteredResult = visibility.evaluateWithViews(result, direction, visibility);
            return filteredResult;
        }
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addSiblingMatchesToResult(Object o, List result) {
		List parents = UIQueryUtils.parents(o);
		if (parents != null && !parents.isEmpty()) {
			Object immediateParent = parents.get(0);
			for (Object v : UIQueryUtils.subviews(immediateParent)) {
				if (v != o && match(v)) {
					result.add(v);
				}
			}									
		}		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addParentMatchesToResult(Object o, List result) {
		for (Object parent : UIQueryUtils.parents(o))
		{
			if (match(parent))
			{
				result.add(parent);
			}
		}		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addChildMatchesToResult(Object o, List result) {
		for (Object child : UIQueryUtils.subviews(o))
		{
			if (match(child))
			{
				result.add(child);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addDecendantMatchesToResult(Object o, List result) {
		if (match(o)) 
		{
			result.add(o);
		}
				
		for (Object child : UIQueryUtils.subviews(o))
		{		
			addDecendantMatchesToResult(child, result);
		}
		
	}
	
	private boolean match(Object o)
	{
		if (this.simpleClassName == null && this.qualifiedClassName == null) {
			return false;
		}
		return matchSimpleClassName(o,this.simpleClassName) ||
				matchQualifiedClassName(o,this.qualifiedClassName);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean matchQualifiedClassName(Object o, Class qualifiedClassName) {
		return qualifiedClassName != null && qualifiedClassName.isAssignableFrom(o.getClass());
	}

	public static boolean matchSimpleClassName(Object o, String simpleClassName) {
		return simpleClassName != null && simpleClassName.equalsIgnoreCase(o.getClass().getSimpleName());
	}
	
	public String toString() {
		if (this.simpleClassName == null && this.qualifiedClassName == null) {
			return "Class[null]";	
		}

		if (this.simpleClassName != null) 
		{
			return "Class["+this.simpleClassName+"]";	
		}
		else 
		{
			return "Class["+this.qualifiedClassName+"]";
		}
		
	}
	
	
}
