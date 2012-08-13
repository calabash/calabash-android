//package sh.calaba.instrumentationbackend.actions.gestures;
//
//import com.jayway.android.robotium.solo.Pincher;
//import com.jayway.android.robotium.solo.Solo;
//
//import sh.calaba.instrumentationbackend.InstrumentationBackend;
//import sh.calaba.instrumentationbackend.Result;
//import sh.calaba.instrumentationbackend.actions.Action;
//
//public class Pinch implements Action {
//	
//	@Override
//	public Result execute(String... args) {
//		String direction = args[0];
//		if(direction.equalsIgnoreCase("together")) {
//			InstrumentationBackend.solo.pinch(Pincher.TOGETHER);
//		} else {
//			InstrumentationBackend.solo.pinch(Pincher.APART);
//		}
//		return Result.successResult();
//	}
//
//	@Override
//	public String key() {
//		return "pinch";
//	}
//}
