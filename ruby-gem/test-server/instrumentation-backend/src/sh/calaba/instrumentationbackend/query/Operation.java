package sh.calaba.instrumentationbackend.query;

public interface Operation {

	public Object apply(Object o) throws Exception;
	public String getName();
}
