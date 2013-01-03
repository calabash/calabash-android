package sh.calaba.instrumentationbackend;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import android.os.Looper;
import sh.calaba.instrumentationbackend.actions.Action;




public class Command {
    private String line;
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
    
    public void setLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	public String toString() {
		return "Command:'Line:" + getLine() + "," + getCommand() + "', arguments:'" + Arrays.toString(getArguments()) + "'";
	}

    public Result execute() {
        final Action action = InstrumentationBackend.actions.lookup(getCommand());

        if ( Looper.getMainLooper().getThread() == Thread.currentThread()) {
            return action.execute(getArguments());
        }

        final AtomicReference<Result> result = new AtomicReference<Result>();
        InstrumentationBackend.instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                result.set(action.execute(getArguments()));
            }
        });
        return result.get();

    }

}
