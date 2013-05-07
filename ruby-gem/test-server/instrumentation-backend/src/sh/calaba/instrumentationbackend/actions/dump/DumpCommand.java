package sh.calaba.instrumentationbackend.actions.dump;

import java.util.Map;
import java.util.concurrent.Callable;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.json.JSONUtils;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;

public class DumpCommand implements Action {

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result execute(String... args) {

		Map<?, ?> dumpTree = (Map) UIQueryUtils
				.evaluateSyncInMainThread(new Callable() {
					public Object call() throws Exception {
						return UIQueryUtils.dump();
					}
				});

		return new Result(true, JSONUtils.asJson(dumpTree));

	}

	public String key() {
		return "dump";
	}	

}
