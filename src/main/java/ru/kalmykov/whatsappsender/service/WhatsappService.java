package ru.kalmykov.whatsappsender.service;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.Exception.NotFoundException;
import ru.kalmykov.whatsappsender.common.lifecycle.Startable;
import ru.kalmykov.whatsappsender.repository.client.WhatsappClient;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;

import static ru.kalmykov.whatsappsender.repository.client.WhatsappClient.isStale;

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


    boolean enterGroup(String groupTitle) throws InterruptedException {
        WebElement groupReference = findGroupReference(groupTitle);
        if (groupReference != null) {
            groupReference.click();
            LOGGER.debug("ENTERED GROUP");
            return true;
        }
        return false;
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

    void changeFocus(WebElement element) {
        element.sendKeys(Keys.TAB);
    }

    @Nullable
    private WebElement findGroupReference(String groupTitle) throws InterruptedException {
        Set<WebElement> allGroupReferences = new HashSet<>();
        WebElement zeroGroupReference =
                whatsappClient.getGroupReferences()
                        .stream()
                        .filter(e -> {
                            return extractPosition(e) == 0;
                        })
                        .findFirst()
                        .orElse(null);
        changeFocus(whatsappClient.findMain());
        return zeroGroupReference;
    }

    private static int extractPosition(WebElement webElement) {
        String style = webElement.getAttribute("style");
        String translation = StringUtils.substringBetween(style, "translateY(", "px)");
        if (translation == null) {
            throw new NotFoundException("#TRANSLATION.NOT.FOUND; Translation не найдено");
        }
        return Integer.valueOf(translation) / 72;
    }

    private static String extractGroupTitle(WebElement webElement) {
        String[] lines = webElement.getText().split("\\r?\\n");
        if (lines.length > 0) {
            return lines[0];
        } else return "";
    }
}
