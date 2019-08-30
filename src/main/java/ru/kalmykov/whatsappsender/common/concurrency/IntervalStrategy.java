package ru.kalmykov.whatsappsender.common.concurrency;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;

public interface IntervalStrategy {

    long getNextIntervalMs();

    IntervalStrategy NO_WAIT = () -> 0L;

    @Nonnull
    static IntervalStrategy createFixed(long intervalMs) {
        return () -> intervalMs;
    }

    @Nonnull
    static IntervalStrategy createDefaultPerturbated(long intervalMs) {
        return createPerturbatedByFraction(intervalMs, 0.02);
    }

    @Nonnull
    static IntervalStrategy createPerturbatedByFraction(long intervalMs, double downDeviationFraction) {
        checkArgument(downDeviationFraction >= 0.0 && downDeviationFraction <= 1.0);
        return createPerturbated(intervalMs, intervalMs * downDeviationFraction);
    }

    @Nonnull
    static IntervalStrategy createPerturbated(long intervalMs, double downDeviationMs) {
        checkArgument(downDeviationMs <= (double) intervalMs);
        return () -> intervalMs - (long) (downDeviationMs * ThreadLocalRandom.current().nextDouble());
    }
}
