package sh.calaba.instrumentationbackend.actions.application;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.os.Handler;
import android.os.Looper;

public class UIPing implements Action {

	private static final int DEFAULT_TIMEOUT = 5000;

    private final Handler mainLooperHandler = new Handler(Looper.getMainLooper());
    private final ReentrantLock lock = new ReentrantLock();    
    private final Condition pingCondition  = lock.newCondition(); 
    
    private final Runnable pingerRunnable = new Runnable() {
        @Override
        public void run() {
        	try {
        		lock.lockInterruptibly();                		
        	} catch (InterruptedException e) {
        		Thread.currentThread().interrupt();        		
        	} finally {
        		pingCondition.signal();        		
        		lock.unlock();
        	}               
        }
    };

    
	@Override
	public Result execute(String... args) {
		try {
    		lock.tryLock(5, TimeUnit.SECONDS);
    		long timeBefore = System.nanoTime();
    		mainLooperHandler.post(pingerRunnable);
    		pingCondition.await();
    		long timeAfter = System.nanoTime();
    		return new Result(true,""+TimeUnit.MILLISECONDS.convert(timeAfter-timeBefore, TimeUnit.NANOSECONDS));
    		
    	} catch (InterruptedException e) {
    		Thread.currentThread().interrupt();
    		return new Result(false,"5");
    	} finally {
    		lock.unlock();
    	} 		
	}

	@Override
	public String key() {		
		return "uiping";
	}

}
