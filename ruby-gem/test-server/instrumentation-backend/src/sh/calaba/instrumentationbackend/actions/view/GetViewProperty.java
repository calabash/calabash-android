package sh.calaba.instrumentationbackend.actions.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.wait.WaitForViewById;
import android.view.View;

/**
 * eg: performAction( 'get_view_property', 'my_view', 'visibility')  => visible, invisible or gone
 * 
 * @see AssertViewProperty
 * @author Nicholas Albion
 */
public class GetViewProperty extends WaitForViewById implements Action {

	@Override
	public Result execute(String... args) {
		String viewId = args[0];
        
        try {
        	View view = getViewById(viewId, 60000);
        	if( view != null ) {
        		String propertyName = args[1];
        		return getPropertyValue(propertyName, view, args);
        	} else {
        		return new Result(false, "Timed out while waiting for view with id:'" + viewId + "'");
        	}
        } catch( Exception e ) {
    		return Result.fromThrowable(e);
        }
	}
	
	/**
	 * @param propertyName
	 * @param view
	 * @param args - used by {@link AssertViewProperty#getPropertyValue}
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected Result getPropertyValue( String propertyName, View view, String[] args ) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if( "visibility".equals(propertyName) ) {
			String value;
			
			switch( view.getVisibility() ) {
			case View.VISIBLE:
				value = "visible";
				break;
			case View.INVISIBLE:
				value = "invisible";
				break;
			case View.GONE:
				value = "gone";
				break;
			default:
				value = Integer.toString(view.getVisibility());
			}
			
			return new Result( true, value );
		} else {
			// resort to reflection
			Class<? extends View> clazz = view.getClass();
			Method method = null;
			try {
				String methodName = propertyName;
				method = clazz.getMethod( methodName );
			} catch( NoSuchMethodException e ) {
				try {
					String methodName = "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
					method = clazz.getMethod( methodName );
				} catch( NoSuchMethodException e2 ) {
					String methodName = "is" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
					method = clazz.getMethod( methodName );
				}
			}
			Object valueObj = method.invoke(view);
			return processProperty( propertyName, valueObj, args );
		}
	}
	
	/**
	 * @param propertyName
	 * @param propertyValue
	 * @param args - used by {@link AssertViewProperty#getPropertyValue}
	 * @return a successful result with the requested property as the <code>message</code> field
	 */
	protected Result processProperty( String propertyName, Object propertyValue, String[] args ) {
		String message = (propertyValue == null) ? "null" : propertyValue.toString();
		return new Result( true, message );
	}

	@Override
	public String key() {
		return "get_view_property";
	}
}
