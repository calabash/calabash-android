package sh.calaba.instrumentationbackend;

import java.util.ArrayList;
import java.util.List;

public class Result {
	private static Result successResult = new Result(true);
	
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
    	return new Result(false, t.getMessage());
    }
    
    public static Result successResult() {
    	return successResult;
    }
    
    public String toString() {
        return "Success: " + success + ", message: " + message;
    }
}
