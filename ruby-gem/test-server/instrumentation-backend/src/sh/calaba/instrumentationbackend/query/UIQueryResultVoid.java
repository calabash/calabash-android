package sh.calaba.instrumentationbackend.query;

import java.util.HashMap;
import java.util.Map;


public class UIQueryResultVoid {
	public static final UIQueryResultVoid instance = new UIQueryResultVoid();

	private UIQueryResultVoid() {}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object asMap(String methodName, Object receiver,
			String errorMessage) {
		Map map = new HashMap();
		map.put("error", errorMessage);
		map.put("methodName", methodName);
		map.put("receiverClass", receiver.getClass().getName());
		map.put("receiverString", receiver.toString());
		return map;
	}
}