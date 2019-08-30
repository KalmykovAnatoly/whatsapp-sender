package ru.kalmykov.whatsappsender.common.lifecycle;

import com.google.common.base.Throwables;
import org.springframework.context.SmartLifecycle;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractLifecycle implements SmartLifecycle {

    private volatile boolean running;

    @Override
    public void start() {
        if (running) {
            return;
        }
        try {
            onStart();
            this.running = true;//setup only on success
        } catch (Throwable t) {
            //always throw exceptions at start
            Throwables.throwIfUnchecked(t);
            throw new RuntimeException(t);
        }
    }

    @Override
    public void stop() {
        //nothing, ignored according to spring docs when used <SmartLifecycle>
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public abstract void onStart() throws Exception;

    public abstract void onStop() throws Exception;
}
