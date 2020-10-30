package ru.kalmykov.whatsappsender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.entity.Message;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@ParametersAreNonnullByDefault
public class Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private final WhatsappService whatsappService;
    private final ScheduledExecutorService scheduler;
    private final long processingInterval;
    private final AtomicBoolean debug = new AtomicBoolean(false);
    private final AtomicBoolean arthurMode = new AtomicBoolean(false);
    private final Set<Message> messages = new LinkedHashSet<>();
    private final Pattern groupNamePattern;
    private final Pattern debugGroupNamePattern;

    public Processor(
            WhatsappService whatsappService,
            @Value("${processor.processing-interval}") long processingInterval,
            @Value("${settings.group-name-pattern}") String groupNamePattern,
            @Value("${settings.debug-group-name-pattern}") String debugGroupNamePattern
    ) {
        this.whatsappService = whatsappService;
        this.processingInterval = processingInterval;
        this.groupNamePattern = Pattern.compile(groupNamePattern);
        this.debugGroupNamePattern = Pattern.compile(debugGroupNamePattern);
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.start();
    }

    private void start() {
        whatsappService.enterWhatsapp();

        whatsappService.enterGroup(this.groupNamePattern);

        String currentGroupTitle = whatsappService.currentGroupTitle();
        LOGGER.debug("Current group title: {}", currentGroupTitle);

        messages.addAll(new LinkedHashSet<>(whatsappService.getMessages()));

        scheduler.scheduleWithFixedDelay(this::process, 0, processingInterval, TimeUnit.MILLISECONDS);
    }

    private void process() {
        List<Message> current = whatsappService.getMessages();
        Set<Message> currentSet = current.stream()
                .filter(message -> {
                    if (messages.contains(message)) {
                        return false;
                    } else {
                        LOGGER.trace("New message: {}", message);
                        return true;
                    }
                })
                .peek(this::action)
                .collect(Collectors.toSet());
        messages.addAll(currentSet);
    }

    private void action(@Nullable Message message) {

        if (message != null && "Я".equalsIgnoreCase(message.author) && message.text != null) {
            switch (message.text) {
                case "/debug":
                    LOGGER.debug("/debug received. current: {}", debug.get());
                    debug.set(!debug.get());
                    whatsappService.writeToChat("debug: " + (debug.get() ? "on" : "off"));
                    break;
                case "/time":
                    if (debug.get()) {
                        whatsappService.writeToChat("time: " + LocalDateTime.now());
                    }
                    break;
                case "/arthur_mode":
                    arthurMode.set(!arthurMode.get());
                    whatsappService.writeToChat("arthur_mode: " + (arthurMode.get() ? "on" : "off"));
                    break;
                default:
                    if (arthurMode.get()) {
                        Arrays.stream(message.text.split("\\s+")).forEach(whatsappService::writeToChat);
                    }
            }

        }
    }
}
