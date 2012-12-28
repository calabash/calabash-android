package sh.calaba.instrumentationbackend;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sh.calaba.org.codehaus.jackson.map.ObjectMapper;

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
	final List results;
        

	@SuppressWarnings({ "rawtypes" })
	public FranklyResult(boolean success, List results, String reason,
			String detail) {
		super();
		this.success = success;
		this.results = results;		
		this.reason = reason;
		this.detail = detail;
	}

	public static FranklyResult fromThrowable(Throwable t) {
    	CharArrayWriter caw = new CharArrayWriter();
    	t.printStackTrace(new PrintWriter(caw));		
		return new FranklyResult(false,Collections.EMPTY_LIST,t.getMessage(),caw.toString());
    }
    
    public static FranklyResult emptyResult() {
    	return new FranklyResult(true,Collections.EMPTY_LIST,null,null);
    }

    public static FranklyResult failedResult(String message,String detail) {
        return new FranklyResult(false,Collections.EMPTY_LIST,message,detail);
    }
    
    public String asJson() {
        ObjectMapper mapper = new ObjectMapper();

        try {        	
            return mapper.writeValueAsString(asMap());
        } catch (IOException e) {
            throw new RuntimeException("Could not convert result to json", e);
        }
    }

	public Map<String,Object> asMap() 
	{
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("outcome", this.success ? "SUCCESS" : "ERROR");
		
		if (this.success) 
		{
			result.put("results",this.results);	
		}
		else 
		{
			result.put("reason", this.reason);
			if (this.detail != null)
			{
				result.put("detail", this.detail);
			}						
		}
		return result;
	}

	@Override
	public String toString() 
	{
		return "FranklyResult [success=" + success + ", reason=" + reason
				+ ", details=" + detail + ", results=" + results + "]";
	}

	@SuppressWarnings("rawtypes")
	public static FranklyResult successResult(List result) {
		return new FranklyResult(true, result, null,null);
	}    
}
