package sh.calaba.instrumentationbackend.query.ast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import sh.calaba.instrumentationbackend.query.antlr.UIQueryParser;

public class UIQueryASTFilter implements UIQueryAST {
	public final String propertyName;	
	public final Object value;
	
	public UIQueryASTFilter(String property, Object value) 
	{
		if (property == null) {throw new IllegalArgumentException("Cannot instantiate Filter with null property name");}
		this.propertyName = property;
		this.value = value;
	}

	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public List evaluateWithViewsAndDirection(List inputViews,
			UIQueryDirection direction) {
		List result = new ArrayList(8);
				
		for (Object o : inputViews) 
		{		
			if (this.propertyName == "marked")
			{
				if (isMarked(o,this.value))
				{
					result.add(o);
				}
			}
			else 
			{
				Method propertyAccessor = UIQueryUtils.hasProperty(o, this.propertyName);
				if (propertyAccessor != null)
				{
					Object value = UIQueryUtils.getProperty(o, propertyAccessor);
					if (value == this.value ||
							(value != null && value.equals(this.value))) {
						result.add(o);
					}
				}	
			}
			
		}
		
		
		return result;
	}

	private boolean isMarked(Object o, Object expectedValue) {
		// TODO Auto-generated method stub
		return false;
	}

	public static UIQueryASTFilter fromAST(CommonTree step) {
		CommonTree prop = (CommonTree) step.getChild(0);
		CommonTree val = (CommonTree) step.getChild(1);
		
		switch(val.getType())
		{
			case UIQueryParser.STRING:	
				return new UIQueryASTFilter(prop.getText(), val.getText());
			case UIQueryParser.INT:
				return new UIQueryASTFilter(prop.getText(), Integer.parseInt(val.getText(), 10));
			default:
				throw new IllegalArgumentException("Unable to parse value type:" + val.getType()+ " text "+val.getText());
				
		}
		
	}
	
}
