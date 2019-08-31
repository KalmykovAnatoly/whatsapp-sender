package ru.kalmykov.whatsappsender.service;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.common.concurrency.IntervalStrategy;
import ru.kalmykov.whatsappsender.common.concurrency.LoopRunnable;
import ru.kalmykov.whatsappsender.common.lifecycle.AbstractLifecycle;
import ru.kalmykov.whatsappsender.repository.client.WhatsappClient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@ParametersAreNonnullByDefault
public class Processor extends AbstractLifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private final WhatsappClient whatsappClient;
    private final ExecutorService executorService;

    public Processor(
            WhatsappClient whatsappClient
    ) {
        this.whatsappClient = whatsappClient;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onStart() throws Exception {
        whatsappClient.start();

        WebElement groupReference = whatsappClient.findGroupReference();
        if (groupReference != null) {
            groupReference.click();
            LOGGER.debug("ENTERED GROUP");
        }

        executorService.submit(new LoopRunnable(
                IntervalStrategy.createDefaultPerturbated(1000),
                this::isStopped,
                this::process
        ));
    }

    @Override
    public void onStop() throws Exception {

    }

    private void process() {
        LOGGER.debug("PROCESSING...");
    }

    private boolean isStopped() {
        return executorService.isShutdown();
    }
}
