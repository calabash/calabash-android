package sh.calaba.instrumentationbackend.query.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents a List where at most one result is a Future.
 * This class is itself a Future which is realized if the list contains no future,
 * and if the list does contain a Future, this object is realized once that Future is.
 * 
 * We can think of it as a way to "lift" a list containing a Future into the set of Futures.
 * @author krukow
 *
 */
@SuppressWarnings("rawtypes")
public class PartialFutureList implements Future {
	
	
	private final Future theFutureResult;	
	private final List originalList;	
	
	public PartialFutureList(List listWithOneFuture) {
		this.theFutureResult = extractFuture(listWithOneFuture);
		this.originalList = listWithOneFuture;
	}

	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (theFutureResult == null) {
			return false;
		}
		else {
			return theFutureResult.cancel(mayInterruptIfRunning);
		}
	}
	
	@Override
	public Object get() throws InterruptedException, ExecutionException {
		if (theFutureResult == null) {
			return originalList;
		}		
		return fillListWithResult(theFutureResult.get());
	}

	
	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		if (theFutureResult == null) {
			return originalList;
		}		
		return fillListWithResult(theFutureResult.get(timeout,unit));
	}

	@Override
	public boolean isCancelled() {
		if (theFutureResult == null) {
			return false;
		}		
		return theFutureResult.isCancelled();
	}

	@Override
	public boolean isDone() {
		if (theFutureResult == null) {
			return false;
		}		
		return theFutureResult.isDone();
	}
	
	@SuppressWarnings({ "unchecked" })
	private Object fillListWithResult(Object futureResultObject) {
		List result = new ArrayList(this.originalList.size());
		for (Object o : this.originalList) {
			if (o instanceof Future) {//Assume only one future
				result.add(futureResultObject);
			} else {
				result.add(o);
			}
		}
		return result;
	}
	
	private Future extractFuture(List list) {
		for (Object o : list) {
			if (o instanceof Future) {
				return (Future) o;
			}
		}
		return null;
	}



}
