package ru.kalmykov.whatsappsender.common.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.time.Clock;

public class ClockUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClockUtils.class);

    public static final Clock UTC = Clock.systemUTC();
    public static final Clock UNI_CLOCK = UTC;

    public static void waitTimeout(long timeoutMs) throws InterruptedException {
        waitDeadline(toDeadlineTs(timeoutMs, UNI_CLOCK), UNI_CLOCK);
    }

    public static long toDeadlineTs(long timeoutMs, @Nonnull Clock clock) {
        return toDeadlineTs(timeoutMs, clock.millis());
    }

    public static void waitDeadline(long deadlineTs, @Nonnull Clock clock) throws InterruptedException {
        long now = clock.millis();
        while (now < deadlineTs) {
            Thread.sleep(deadlineTs - now);
            now = clock.millis();
        }
    }

    public static long toDeadlineTs(long timeoutMs, long now) {
        return isTimeoutExist(timeoutMs) ? now + timeoutMs : now;
    }

    public static boolean isTimeoutExist(long timeoutMs) {
        return timeoutMs > 0L;
    }
}
