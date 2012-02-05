package sh.calaba.instrumentationbackend;

import java.util.Arrays;

import sh.calaba.instrumentationbackend.actions.Action;




public class Command {
    
    private String command;
    private String[] arguments;
    
    public void setCommand(String command) {
    	this.command =  command;
    }
    
    public void setArguments(String[] arguments) {
    	this.arguments = arguments;
    }
    
    public String getCommand() {
    	return command;
    }
    
    public String[] getArguments() {
    	return arguments;
    }
    
    public String toString() {
		return "Command:'" + getCommand() + "', arguments:'" + Arrays.toString(getArguments()) + "'";
	}

    public Result execute() {
        Action action = InstrumentationBackend.actions.lookup(getCommand());
        return action.execute(getArguments());
    }

}
