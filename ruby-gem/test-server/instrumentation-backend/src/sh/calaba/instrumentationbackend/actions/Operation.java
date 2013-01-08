package sh.calaba.instrumentationbackend.actions;

public interface Operation {

	public Object apply(Object o) throws Exception;
	public String getName();
}
