package ru.kalmykov.whatsappsender.service;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.entity.GroupReference;
import ru.kalmykov.whatsappsender.entity.Message;
import ru.kalmykov.whatsappsender.exception.NotFoundException;
import ru.kalmykov.whatsappsender.repository.client.WhatsappClient;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@ParametersAreNonnullByDefault
public class WhatsappService {
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

    public void enterWhatsapp() {
        whatsappClient.enterWhatsapp();
    }

    void enterGroup(Pattern groupTitlePattern) {
        WebElement groupReference = findGroupReference(groupTitlePattern);
        if (groupReference == null) {
            throw new NotFoundException("#group.not.found; Группа не найдена");
        }
        groupReference.click();
        LOGGER.debug("ENTERED GROUP");
    }

    public String currentGroupTitle() {
        WebElement webElement = whatsappClient.findCurrentGroupHeader();
        if (webElement == null) {
            throw new NotFoundException("#GROUP.NOT.FOUND; Группа не найдена");
        }
        return webElement.getText();
    }

    void writeToChat(String text) {
        WebElement chatInput = whatsappClient.findChatInput();
        if (chatInput == null) {
            LOGGER.warn("Chat input not found");
            return;
        }
        chatInput.sendKeys(text);
        WebElement sendButton = whatsappClient.findSendButton();
        if (sendButton == null) {
            LOGGER.warn("Send button not found");
            return;
        }
        sendButton.click();
    }

    void scrollUpChatOutput(int number) {
        whatsappClient.scrollUpChatOutput(number);
    }

    List<Message> getMessages() {
        return whatsappClient.getMessages()
                .stream()
                .map(messageParser::parseMessage)
                .collect(Collectors.toList());
    }

    @Nullable
    private WebElement findGroupReference(Pattern groupTitlePattern) {
        Set<GroupReference> allGroupReferences = new HashSet<>();
        GroupReference groupReference;
        while (true) {
            List<GroupReference> currentGroups = whatsappClient.getGroupReferences()
                    .stream()
                    .map(GroupReference::new)
                    .collect(Collectors.toList());
            LOGGER.debug("Current groups. size: {}, groups: {}", currentGroups.size(), currentGroups);

            if (allGroupReferences.containsAll(currentGroups)) {
                LOGGER.debug("END OF GROUPS");
                return null;
            }
            groupReference = currentGroups
                    .stream()
                    .filter(e -> groupTitlePattern.matcher(e.title).find())
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
