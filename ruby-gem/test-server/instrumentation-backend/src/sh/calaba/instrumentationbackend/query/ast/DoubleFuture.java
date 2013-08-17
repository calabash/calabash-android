package sh.calaba.instrumentationbackend.query.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient.WebFuture;

@SuppressWarnings("rawtypes")
public class DoubleFuture implements Future {

	private final WebFuture f1;
	private final WebFuture f2;

	public DoubleFuture(WebFuture f1, WebFuture f2) {
		this.f1 = f1;
		this.f2 = f2;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return f1.cancel(mayInterruptIfRunning) && f2.cancel(mayInterruptIfRunning);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Object get() throws InterruptedException, ExecutionException {
		Object o1 = f1.get();
		Object o2 = f2.get();
		
		List res = new ArrayList();
		res.add(o1);
		res.add(o2);
		return res;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		Object o1 = f1.get(timeout, unit); ///this is actually double timeout
		Object o2 = f2.get(timeout, unit);
		
		List res = new ArrayList();
		res.add(o1);
		res.add(o2);
		return res;
	}

	@Override
	public boolean isCancelled() {
		return f1.isCancelled() || f2.isCancelled();
	}

	@Override
	public boolean isDone() {
		return f1.isDone() && f2.isDone();
	}

}
