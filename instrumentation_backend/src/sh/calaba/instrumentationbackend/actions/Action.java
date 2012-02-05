package sh.calaba.instrumentationbackend.actions;

import sh.calaba.instrumentationbackend.Result;


public interface Action {

	Result execute(String... args);
	
	String key();
}
