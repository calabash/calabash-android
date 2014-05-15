package sh.calaba.instrumentationbackend.actions.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.query.Operation;
import sh.calaba.instrumentationbackend.query.InvocationOperation;
import sh.calaba.instrumentationbackend.query.QueryResult;
import sh.calaba.instrumentationbackend.query.UIQueryResultVoid;


public class Backdoor implements Action {

	private static final String TAG = "Backdoor";

	@Override
	public Result execute(String... args) {
		if (args.length != 2) {
			return Result.failedResult("You must provide method name and an argument.");
		}

		String methodName = args[0];
		List arguments = new ArrayList(1);
		arguments.add(args[1]);
		// create invocation operation to call method
		Operation op = new InvocationOperation(methodName, arguments);
		// get an application object to call operation on
		Context app = InstrumentationBackend.solo.getCurrentActivity().getApplication();
		String backdoorResult = null;
		try {
			backdoorResult = (String)op.apply(app);
		} catch (Exception e) {
			android.util.Log.e(TAG, android.util.Log.getStackTraceString(e));
			return Result.failedResult("No such backdoor method found: public String " + op.getName() + "(String arg)");
		}

		// set backdoor result as bonus
		Result result = Result.successResult();
		result.addBonusInformation(backdoorResult);
		return result;
	}

	@Override
	public String key() {
		return "backdoor";
	}

}
