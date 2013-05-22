package sh.calaba.instrumentationbackend.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ViewDump {

	public Map<?,?> dumpWithoutElements() {
		Map<?, ?> dumpTree = (Map) UIQueryUtils.evaluateSyncInMainThread(new Callable() {
			public Object call() throws Exception {
				return UIQueryUtils.dump();
			}
		});
		
		return sameTreeWithoutElements(dumpTree);
		
	}

		
	
	private Map<?, ?> sameTreeWithoutElements(Map<?, ?> dump) {
		Map node = UIQueryUtils.mapWithElAsNull(dump);
		List<Map> nodeChildren = (List<Map>) node.get("children");
		List<Map> childrenNoEl = new ArrayList<Map>(nodeChildren.size());
		for (Map child : nodeChildren) {
			childrenNoEl.add(sameTreeWithoutElements(child));
		}
		node.put("children",childrenNoEl);
		return node;
	}

	
}
