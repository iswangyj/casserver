package org.jasig.cas.monitor;

import javax.validation.constraints.NotNull;
import java.util.concurrent.*;

/**
 * @author SxL
 * Created on 9/25/2018 2:57 PM.
 */
public abstract class AbstractPoolMonitor extends AbstractNamedMonitor<PoolStatus> {
    public static final int DEFAULT_MAX_WAIT = 3000;
    private int maxWait = 3000;
    @NotNull
    private ExecutorService executor;

    public AbstractPoolMonitor() {
    }

    public void setExecutor(ExecutorService executorService) {
        this.executor = executorService;
    }

    public void setMaxWait(int time) {
        this.maxWait = time;
    }

    @Override
    public PoolStatus observe() {
        Future<StatusCode> result = this.executor.submit(new AbstractPoolMonitor.Validator());
        String description = null;

        StatusCode code;
        try {
            code = (StatusCode)result.get((long)this.maxWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException var5) {
            code = StatusCode.UNKNOWN;
            description = "Validator thread interrupted during pool validation.";
        } catch (TimeoutException var6) {
            code = StatusCode.WARN;
            description = String.format("Pool validation timed out.  Max wait is %s ms.", this.maxWait);
        } catch (Exception var7) {
            code = StatusCode.ERROR;
            description = var7.getMessage();
        }

        return new PoolStatus(code, description, this.getActiveCount(), this.getIdleCount());
    }

    protected abstract StatusCode checkPool() throws Exception;

    protected abstract int getIdleCount();

    protected abstract int getActiveCount();

    private class Validator implements Callable<StatusCode> {
        private Validator() {
        }

        @Override
        public StatusCode call() throws Exception {
            return AbstractPoolMonitor.this.checkPool();
        }
    }
}
