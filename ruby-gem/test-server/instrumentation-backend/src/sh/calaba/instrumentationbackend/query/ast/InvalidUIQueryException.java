package sh.calaba.instrumentationbackend.query.ast;

@SuppressWarnings("serial")
public class InvalidUIQueryException extends RuntimeException {

	public InvalidUIQueryException(String query) {
		super(query);
	}

}
