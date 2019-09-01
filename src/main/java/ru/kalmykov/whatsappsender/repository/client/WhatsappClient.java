package ru.kalmykov.whatsappsender.repository.client;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.common.lifecycle.Startable;
import ru.kalmykov.whatsappsender.service.ChromeWebDriver;
import ru.kalmykov.whatsappsender.value.Xpath;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.kalmykov.whatsappsender.value.Xpath.*;

@Service
@ParametersAreNonnullByDefault
public class WhatsappClient implements Startable {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhatsappClient.class);

    private final ChromeWebDriver chromeWebDriver;
    private final String endpoint;

    public WhatsappClient(
            ChromeWebDriver chromeWebDriver,
            @Value("${endpoint.whatsapp}") String endpoint
    ) {
        this.chromeWebDriver = chromeWebDriver;
        this.endpoint = endpoint;
    }

    @Override
    public void start() throws Exception {
        chromeWebDriver.get(endpoint);
    }

    @Override
    public void stop() throws Exception {
        chromeWebDriver.close();
    }

    @Nullable
    public WebElement findScanMe() {
        return safeFinding(SCAN_ME);
    }

    @Nullable
    public WebElement findChatInput() {
        return safeFinding(CHAT_INPUT);
    }

    @Nullable
    public WebElement findCurrentGroupHeader() {
        return safeFinding(CURRENT_GROUP_HEADER);
    }

    @Nullable
    public WebElement findScrollButton() {
        return safeFinding(SCROLL_BUTTON);
    }

    @Nullable
    public WebElement findSendButton() {
        return safeFinding(SEND_BUTTON);
    }

    @Nullable
    public WebElement findGroupReference(String groupName) throws InterruptedException {
        Set<WebElement> allGroupReferences = new HashSet<>();
        WebElement groupReference;
        while (true) {
            List<WebElement> currentGroups = getGroupReferences();

            groupReference = currentGroups
                    .stream()
                    .filter(e -> !isStale(e) && groupName.equals(e.getText()))
                    .findFirst()
                    .orElse(null);

            if (groupReference != null || allGroupReferences.containsAll(currentGroups)) {
                break;
            }

            allGroupReferences.addAll(currentGroups);
            LOGGER.debug("GROUPS ADDED: " + currentGroups.size());
            scrollDownPaneSide();
        }
        return groupReference;
    }


    public static boolean isStale(WebElement element) {
        try {
            element.isEnabled();
            return false;
        } catch (StaleElementReferenceException ignored) {
            return true;
        }
    }

    public void scrollUpChatOutput() throws InterruptedException {
        chromeWebDriver.executeScript("document.getElementsByClassName('_1_keJ')[0].scrollBy({top: -600})");
        Thread.sleep(100);
    }

    private void scrollDownPaneSide() throws InterruptedException {
        chromeWebDriver.executeScript("document.getElementById('pane-side').scrollBy({top: 600})");
        Thread.sleep(100);
    }

    private List<WebElement> getGroupReferences() {
        return chromeWebDriver.findElements(Xpath.of(GROUP_REFERENCE));
    }

    @Nullable
    private WebElement safeFinding(Xpath item) {
        try {
            return chromeWebDriver.findElement(Xpath.of(item));
        } catch (Exception e) {
            LOGGER.error("ELEMENT NOT FOUND", e);
            return null;
        }
    }
}
