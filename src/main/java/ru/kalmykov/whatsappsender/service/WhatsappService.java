package ru.kalmykov.whatsappsender.service;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.Exception.NotFoundException;
import ru.kalmykov.whatsappsender.common.lifecycle.Startable;
import ru.kalmykov.whatsappsender.entity.GroupReference;
import ru.kalmykov.whatsappsender.entity.Message;
import ru.kalmykov.whatsappsender.repository.client.WhatsappClient;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.kalmykov.whatsappsender.repository.client.WhatsappClient.isStale;

@Service
@ParametersAreNonnullByDefault
public class WhatsappService implements Startable {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhatsappService.class);

    private final WhatsappClient whatsappClient;
    private final MessageParser messageParser;

    public WhatsappService(
            WhatsappClient whatsappClient,
            MessageParser messageParser
    ) {
        this.whatsappClient = whatsappClient;
        this.messageParser = messageParser;
    }

    @Override
    public void start() throws Exception {
        whatsappClient.start();
        WebElement scanMe = whatsappClient.findScanMe();
        if (scanMe == null) {
            throw new NotFoundException("#SCAN.ME.NOT.FOUND; Scan me не найдена");
        }
        while (!isStale(scanMe)) {
            Thread.sleep(2000);
            LOGGER.debug("WAITING FOR SCAN");
        }
        LOGGER.debug("SCANNED");
    }

    @Override
    public void stop() throws Exception {
        whatsappClient.stop();
    }


    void enterGroup(String groupTitle) {
        WebElement groupReference = findGroupReference(groupTitle);
        if (groupReference == null) {
            throw new NotFoundException("#GROUP.NOT.FOUND; Группа не найдена");
        }
        groupReference.click();
        LOGGER.debug("ENTERED GROUP");
    }

    String currentGroupTitle() {
        WebElement webElement = whatsappClient.findCurrentGroupHeader();
        if (webElement == null) {
            throw new NotFoundException("#GROUP.NOT.FOUND; Группа не найдена");
        }
        return webElement.getText();
    }

    void writeToChat(String text) {
        WebElement chatInput = whatsappClient.findChatInput();
        if (chatInput == null) {
            throw new NotFoundException("#CHAT.INPUT.NOT.FOUND; Chat input не найден");
        }
        chatInput.sendKeys(text);
        WebElement sendButton = whatsappClient.findSendButton();
        if (sendButton == null) {
            throw new NotFoundException("#SEND.BUTTON.NOT.FOUND; Send button не найдена");
        }
        sendButton.click();
    }

    void scrollUpChatOutput(int number) throws InterruptedException {
        whatsappClient.scrollUpChatOutput(number);
    }

    List<Message> getMessages() {
        return whatsappClient.getMessages()
                .stream()
                .map(messageParser::parseMessage)
                .collect(Collectors.toList());
    }

    @Nullable
    private WebElement findGroupReference(String groupTitle) {
        Set<GroupReference> allGroupReferences = new HashSet<>();
        GroupReference groupReference;
        while (true) {
            List<GroupReference> currentGroups = whatsappClient.getGroupReferences()
                    .stream()
                    .map(GroupReference::new)
                    .collect(Collectors.toList());
            LOGGER.debug(currentGroups.toString());

            if (allGroupReferences.containsAll(currentGroups)) {
                LOGGER.debug("END OF GROUPS");
                return null;
            }
            groupReference = currentGroups
                    .stream()
                    .filter(e -> e.title.equals(groupTitle))
                    .findFirst()
                    .orElse(null);

            if (groupReference != null) {
                return groupReference.webElement;
            }

            allGroupReferences.addAll(currentGroups);
            whatsappClient.scrollDownPaneSide(1500);
        }
    }
}
