package sh.calaba.instrumentationbackend;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sh.calaba.instrumentationbackend.json.JSONUtils;
import sh.calaba.instrumentationbackend.query.QueryResult;

/**
 * Represents a response in the Frankly protocol.
 * This protocol is a JSON over HTTP protocol which is
 * used across Calabash iOS, Calabash Android and Frank.
 * @author krukow
 * @see https://github.com/moredip/Frank/blob/master/doc/frankly.md
 *
 */
public class FranklyResult {
	
    final boolean success;
    final String reason;
    final String detail;
    @SuppressWarnings("rawtypes")
	final QueryResult result;
        

	@SuppressWarnings({ "rawtypes" })
	public FranklyResult(boolean success, QueryResult result, String reason, String detail) {
		super();
		this.success = success;
		this.result = result;
		this.reason = reason;
		this.detail = detail;
	}

	public static FranklyResult fromThrowable(Throwable t) {
    	CharArrayWriter caw = new CharArrayWriter();
    	t.printStackTrace(new PrintWriter(caw));

		return new FranklyResult(false, null, t.getMessage(),caw.toString());
    }
    
    public static FranklyResult emptyResult() {
    	return new FranklyResult(true, null, null,null);
    }

    public static FranklyResult failedResult(String message,String detail) {
        return new FranklyResult(false, null, message,detail);
    }
    
    public String asJson() {
        return JSONUtils.asJson(asMap());
    }

	public Map<String, Object> asMap()
	{
		Map<String,Object> map = new HashMap<String, Object>();
        map.put("outcome", success ? "SUCCESS" : "ERROR");
		
		if (success)
		{
            map.put("results", result.asList());
		}
		else 
		{
            map.put("reason", reason);
			if (detail != null)
			{
                map.put("detail", detail);
			}						
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static FranklyResult successResult(QueryResult result) {
		return new FranklyResult(true, result, null,null);
	}    
}
