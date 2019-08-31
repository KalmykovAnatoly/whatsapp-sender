package ru.kalmykov.whatsappsender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.common.concurrency.IntervalStrategy;
import ru.kalmykov.whatsappsender.common.concurrency.LoopRunnable;
import ru.kalmykov.whatsappsender.common.lifecycle.AbstractLifecycle;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@ParametersAreNonnullByDefault
public class Processor extends AbstractLifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private final static String groupName = "Вов - духовное возвышение";

    private final WhatsappService whatsappService;
    private final ExecutorService executorService;

    public Processor(
            WhatsappService whatsappService
    ) {
        this.whatsappService = whatsappService;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onStart() throws Exception {
        whatsappService.start();

        whatsappService.enterGroup(groupName);

        whatsappService.writeToChat("TEST");

        executorService.submit(new LoopRunnable(
                IntervalStrategy.createDefaultPerturbated(1000),
                this::isStopped,
                this::process
        ));
    }

    @Override
    public void onStop() throws Exception {
        executorService.shutdown();
        whatsappService.stop();
    }

    private void process() {
        LOGGER.debug("PROCESSING...");
    }

    private boolean isStopped() {
        return executorService.isShutdown();
    }
}
