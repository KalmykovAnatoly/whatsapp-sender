package ru.kalmykov.whatsappsender.service;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.common.concurrency.IntervalStrategy;
import ru.kalmykov.whatsappsender.common.concurrency.LoopRunnable;
import ru.kalmykov.whatsappsender.common.lifecycle.AbstractLifecycle;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@ParametersAreNonnullByDefault
public class Processor extends AbstractLifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private final WhatsappStarter whatsappStarter;
    private final Parser parser;
    private final ExecutorService executorService;

    public Processor(
            WhatsappStarter whatsappStarter,
            Parser parser
    ) {
        this.whatsappStarter = whatsappStarter;
        this.parser = parser;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onStart() throws Exception {
        whatsappStarter.start();

        WebElement group = parser.getGroup("Собираемся у святыни");
        if (group!=null){
            group.click();
        }
        executorService.submit(new LoopRunnable(
                IntervalStrategy.createDefaultPerturbated(1000),
                this::isStopped,
                () -> this.process(group)
        ));
    }

    @Override
    public void onStop() throws Exception {

    }

    private void process(@Nullable WebElement element) {
        LOGGER.debug("PROCESSING...");
        LOGGER.debug(element != null ? element.getText() : null);
    }

    private boolean isStopped() {
        return executorService.isShutdown();
    }
}
