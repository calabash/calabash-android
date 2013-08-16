package sh.calaba.instrumentationbackend.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.json.JSONUtils;
import sh.calaba.instrumentationbackend.query.ast.DoubleFuture;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import sh.calaba.org.codehaus.jackson.JsonParseException;
import sh.calaba.org.codehaus.jackson.map.JsonMappingException;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import android.util.Log;

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
		List nodeChildren = (List) node.get("children");
		List<Map> childrenNoEl = new ArrayList<Map>(nodeChildren.size());
				
		for (Object child : nodeChildren) {
			if (child instanceof Map) {
				childrenNoEl.add(sameTreeWithoutElements((Map)child));	
			}
			else {
				Log.i("Calabash", child.toString());
				Future webResults = (Future) child;
				
				try {
					Map webResultsMap = (Map) webResults.get(10,TimeUnit.SECONDS);
					String json = (String) webResultsMap.get("result");
					ObjectMapper mapper = new ObjectMapper();
					List jsonResults = mapper.readValue(json, List.class);
					for(Object m : jsonResults) {						
						Map domElement = UIQueryUtils.mapWithElAsNull(UIQueryUtils.serializeViewToDump(m));
						childrenNoEl.add(domElement);	
					}
					
							
				} catch (InterruptedException e) {
					
				} catch (ExecutionException e) {
					
				} catch (TimeoutException e) {
					
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}			
		}
		node.put("children",childrenNoEl);
		return node;
		
	
	}

	
}
