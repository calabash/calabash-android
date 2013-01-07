package sh.calaba.instrumentationbackend.query;

import java.lang.reflect.Method;
import java.util.Map;

import sh.calaba.instrumentationbackend.actions.Operation;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.view.View;

public class PropertyOperation implements Operation {

	public final String propertyName;	
	
	public PropertyOperation(String propertyName) {
		super();
		this.propertyName = propertyName;
	}


	@SuppressWarnings({ "rawtypes" })
	@Override
	public Object apply(Object o) {
		if (o instanceof Map) {
			Map objAsMap = (Map) o;
			if (objAsMap.containsKey(propertyName)) {											
				return objAsMap.get(propertyName);
			} else {
				return UIQueryResultVoid.instance.asMap(
						propertyName, o, "No key for "
								+ propertyName + ". Keys: "
								+ (objAsMap.keySet().toString()));
			}
		} else {
			if (o instanceof View && "id".equals(propertyName)) {
				return UIQueryUtils.getId((View) o);
			} else {
				Method m = UIQueryUtils
						.hasProperty(o, propertyName);
				if (m != null) {
					try {
						return m.invoke(o);
					} catch (Exception e) {
						e.printStackTrace();
						return (UIQueryResultVoid.instance
								.asMap(propertyName, o,
										"Results in exception " + e.getMessage()));
					}
				} else {
					return (UIQueryResultVoid.instance
							.asMap(propertyName, o,
									"NO accessor for "
											+ propertyName));
				}
			}
		}

	}

}
