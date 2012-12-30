package sh.calaba.instrumentationbackend.query.ast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.CommonTree;

import sh.calaba.instrumentationbackend.actions.webview.Query;
import sh.calaba.instrumentationbackend.actions.webview.QueryHelper;
import sh.calaba.instrumentationbackend.query.antlr.UIQueryParser;
import android.view.View;
import android.webkit.WebView;

public class UIQueryASTWith implements UIQueryAST {
	public final String propertyName;	
	public final Object value;
	
	public UIQueryASTWith(String property, Object value) 
	{
		if (property == null) {throw new IllegalArgumentException("Cannot instantiate Filter with null property name");}
		this.propertyName = property;
		this.value = value;
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	public List evaluateWithViewsAndDirection(List inputViews,
			UIQueryDirection direction) {
		List result = new ArrayList(8);
					
		for (int i=0;i<inputViews.size();i++)
		{
			Object o = inputViews.get(i);
			
			if (o instanceof WebView) 
			{
				evaluateForWebView((WebView) o,result);
			}
			else
			{
				evaluateForObject(o,result,i);
			}
	
		}
		
		
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void evaluateForObject(Object o, List result, int index) {
		if (this.propertyName.equals("id") && hasId(o,this.value))
		{
			result.add(o);
		}
		else if (this.propertyName.equals("marked") && isMarked(o,this.value))
		{
			result.add(o);
		}
		else if (this.propertyName.equals("index") && this.value.equals(index))
		{
			result.add(o);
		}
		else 
		{
							
			Method propertyAccessor = UIQueryUtils.hasProperty(o, this.propertyName);
			if (propertyAccessor != null)
			{				
				Object value = UIQueryUtils.getProperty(o, propertyAccessor);

				if (value == this.value
						|| (value != null && value.equals(this.value))) 
				{
					result.add(o);
				} 
				else if (this.value instanceof String && this.value.equals(value.toString())) 
				{
					result.add(o);
				}							
			}
			
		}
		
	}

	@SuppressWarnings("unchecked")
	private void evaluateForWebView(WebView o, @SuppressWarnings("rawtypes") List result) 
	{
		if (!(this.value instanceof String))
		{
			return;
		}
		List<Map<String, Object>> queryResult = Query.evaluateQueryInWebView(this.propertyName,(String)this.value,o);
		
		for (Map<String,Object> res : queryResult)
		{
			Map<String,Object> rect = (Map<String, Object>) res.get("rect");
			
			Map<String, Object> newRect = QueryHelper.translateRectToScreenCoordinates(o, rect);
			res.put("rect",newRect);
			
			result.add(res);
		}			
	}

	private boolean hasId(Object o, Object expectedValue) {
		if (! (o instanceof View)) { return false; }
		if (! (expectedValue instanceof String)) { return false; }
		View view = (View) o;
		String expected = (String) expectedValue;
		String id = UIQueryUtils.getId(view);
		return (id != null && id.equals(expected));
	}

	private boolean isMarked(Object o, Object expectedValue) {
		if (! (o instanceof View)) { return false; }
		if (! (expectedValue instanceof String)) { return false; }
		View view = (View) o;
		String expected = (String) expectedValue;
		
		if (hasId(o, expectedValue))
		{
			return true;
		}
		
			
		CharSequence contentDescription = view.getContentDescription();		
		if (contentDescription != null && contentDescription.toString().equals(expected))
		{
			return true;
		}
		
		try {
			Method getTextM = view.getClass().getMethod("getText");
			Object text = getTextM.invoke(view);
			if (text != null && text.toString().equals(expected))
			{
				return true;
			}
			
		} catch (Exception e) {}
		
		return false;
		
	}

	public static UIQueryASTWith fromAST(CommonTree step) {
		CommonTree prop = (CommonTree) step.getChild(0);
		CommonTree val = (CommonTree) step.getChild(1);
		
		switch(val.getType())
		{
			case UIQueryParser.STRING: {
				String textWithPings = val.getText();
				String text = textWithPings.substring(1, textWithPings.length()-1);				
				return new UIQueryASTWith(prop.getText(), text);
			}								
			case UIQueryParser.INT:
				return new UIQueryASTWith(prop.getText(), Integer.parseInt(val.getText(), 10));
			case UIQueryParser.BOOL:{
				String text = val.getText();				
				return new UIQueryASTWith(prop.getText(), Boolean.parseBoolean(text));
			}
			case UIQueryParser.NIL:
				return new UIQueryASTWith(prop.getText(), null);
				
			default:
				throw new IllegalArgumentException("Unable to parse value type:" + val.getType()+ " text "+val.getText());
				
		}
		
	}
	
	
	@Override
	public String toString() {
		return "With["+this.propertyName+":"+this.value+"]";
	}
	
}
