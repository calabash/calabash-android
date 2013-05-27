package sh.calaba.instrumentationbackend.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

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


	public Map<?,?> dumpPathWithoutElements(final List<Integer> path) {
		final AtomicReference<List<Integer>> ref = new AtomicReference<List<Integer>>(path);
		Map<?, ?> dumpTree = (Map) UIQueryUtils.evaluateSyncInMainThread(new Callable() {
			public Object call() throws Exception {
				return UIQueryUtils.dumpByPath(ref.get());
			}
		});
		
		return UIQueryUtils.mapWithElAsNull(dumpTree);						
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
