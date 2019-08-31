package ru.kalmykov.whatsappsender.service;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.common.lifecycle.Startable;
import ru.kalmykov.whatsappsender.repository.client.WhatsappClient;

import javax.annotation.ParametersAreNonnullByDefault;

@Service
@ParametersAreNonnullByDefault
public class WhatsappService implements Startable {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhatsappService.class);

    private final WhatsappClient whatsappClient;

    public WhatsappService(WhatsappClient whatsappClient) {
        this.whatsappClient = whatsappClient;
    }

    @Override
    public void start() throws Exception {
        whatsappClient.start();
    }

    @Override
    public void stop() throws Exception {
        whatsappClient.stop();
    }

    public void writeToChat(String text) {
        WebElement chatInput = whatsappClient.findChatInput();
        chatInput.sendKeys(text);
    }

    public boolean enterGroup(String groupName) throws InterruptedException {
        WebElement groupReference = whatsappClient.findGroupReference(groupName);
        if (groupReference != null) {
            groupReference.click();
            LOGGER.debug("ENTERED GROUP");
            return true;
        }
        return false;
    }
}
