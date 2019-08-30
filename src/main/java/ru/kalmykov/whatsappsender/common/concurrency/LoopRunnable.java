package ru.kalmykov.whatsappsender.common.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kalmykov.whatsappsender.common.action.Action;

import javax.annotation.Nonnull;

public class LoopRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoopRunnable.class);

    public interface IsStopRequiredPredicate {
        boolean isStopRequired();
    }

    @Nonnull
    private final IntervalStrategy intervalStrategy;
    @Nonnull
    private final IsStopRequiredPredicate isStopRequiredPredicate;
    @Nonnull
    private final Action action;

    public LoopRunnable(
            @Nonnull IntervalStrategy intervalStrategy,
            @Nonnull IsStopRequiredPredicate isStopRequiredPredicate,
            @Nonnull Action action
    ) {
        this.intervalStrategy = intervalStrategy;
        this.isStopRequiredPredicate = isStopRequiredPredicate;
        this.action = action;
    }

    @Override
    public void run() {
        boolean first = true;
        boolean error = false;
        long invokeTs = ClockUtils.UNI_CLOCK.millis();
        while (!isStopRequiredPredicate.isStopRequired() && !Thread.currentThread().isInterrupted()) {
            try {
                if (!error) {
                    if (!first) {
                        try {
                            invokeTs += intervalStrategy.getNextIntervalMs();
                        } catch (Throwable t) {
                            LOGGER.error("Exception while getting next interval, wait 100ms and repeat", t);
                            ClockUtils.waitTimeout(100L);
                            continue;
                        }
                    } else {
                        first = false;
                    }
                    ClockUtils.waitDeadline(invokeTs, ClockUtils.UNI_CLOCK);
                }
                invokeTs = ClockUtils.UNI_CLOCK.millis();
                action.execute();
                error = false;
            } catch (InterruptedException ignore) {
                //don't set interrupted flag, ok
                break;
            } catch (Throwable t) {
                error = true;
                LOGGER.error("Exception in loop-runnable body, will be immediately repeated", t);
            }
        }
    }
}
