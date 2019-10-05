package ru.kalmykov.whatsappsender.service;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.common.concurrency.IntervalStrategy;
import ru.kalmykov.whatsappsender.common.concurrency.LoopRunnable;
import ru.kalmykov.whatsappsender.common.lifecycle.AbstractLifecycle;
import ru.kalmykov.whatsappsender.entity.Message;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@ParametersAreNonnullByDefault
public class Processor extends AbstractLifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private final static int INTERVAL_SEC = 1;
    private final static String groupTitle = "ЗАМЕТКИ";

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

        whatsappService.enterGroup(groupTitle);

        Preconditions.checkState(groupTitle.equals(whatsappService.currentGroupTitle()));

        for (Message message : whatsappService.getMessages()) {
            LOGGER.info(message.author + ": " + message.text);
        }

        executorService.submit(new LoopRunnable(
                IntervalStrategy.createDefaultPerturbated(TimeUnit.SECONDS.toMillis(INTERVAL_SEC)),
                this::isStopped,
                this::process
        ));
    }

    @Override
    public void onStop() {
        executorService.shutdown();
        whatsappService.stop();
    }

    private void process() {
//        LOGGER.debug("PROCESSING...");
//        String currentGroupTitle = whatsappService.currentGroupTitle();
//        LOGGER.debug("CURRENT GROUP TITLE: "+ currentGroupTitle);
//        whatsappService.scrollUpChatOutput(1000);
//        whatsappService.writeToChat(currentGroupTitle);
    }

    private boolean isStopped() {
        return executorService.isShutdown();
    }
}
