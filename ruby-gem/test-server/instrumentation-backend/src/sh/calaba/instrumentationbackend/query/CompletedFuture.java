package sh.calaba.instrumentationbackend.query;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CompletedFuture<T> implements Future<T> {
    private final T result;

    public CompletedFuture(final T result) {
        this.result = result;
    }

    @Override
    public boolean cancel(final boolean b) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return this.result;
    }

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return get();
	}
}