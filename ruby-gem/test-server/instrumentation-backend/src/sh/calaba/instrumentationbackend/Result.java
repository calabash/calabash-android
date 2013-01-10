package sh.calaba.instrumentationbackend;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Result {
	
    boolean success;
    String message;
    List<String> bonusInformation = new ArrayList<String>();
    	
    public Result() {
    }
    
    public Result(boolean success) {
		this(success, "");
    }
    
    public Result(boolean success, String message) {
		this.success = success;
		this.message = message;
    }

    public Result(boolean success, List<String> messages) {
		this.success = success;
		this.message = null;
		this.bonusInformation = messages;
    }

    public String getMessage() {
    	return message;
    }
    
    public void setMessage(String message) {
    	this.message = message;
    }
    
    public boolean isSuccess() {
    	return success;
    }
    
    public void setSuccess(boolean success) {
    	this.success = success;
    }
    
    public void addBonusInformation(String information) {
    	bonusInformation.add(information);
	}

    public List<String> getBonusInformation() {
		return bonusInformation;
	}
    
    public void setExtras(List<String> bonusInformation) {
		this.bonusInformation = bonusInformation;
    }
        
    public static Result fromThrowable(Throwable t) {
    	Result r = new Result(false, t.getMessage());
    	CharArrayWriter caw = new CharArrayWriter();
    	t.printStackTrace(new PrintWriter(caw));
    	r.addBonusInformation("Exception stack trace:\n" + caw.toString());
    	return r;
    }
    
    public static Result successResult() {
    	return new Result(true);
    }

    public static Result failedResult() {
        return new Result(false);
    }

    public static Result failedResult(final String message) {
        return new Result(false, message);
    }
    
    public String toString() {
        return "Success: " + success + ", message: " + message;
    }
}
