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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.kalmykov.whatsappsender.value.WhatsappItems.CHAT_INPUT;
import static ru.kalmykov.whatsappsender.value.WhatsappItems.GROUP_REFERENCE;

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
        WebElement scanMeElement = chromeWebDriver.findElement(By.xpath("//img[@alt='Scan me!']"));
        while (!isStale(scanMeElement)) {
            Thread.sleep(2000);
            LOGGER.debug("WAITING FOR SCAN");
        }
        LOGGER.debug("SCANNED");
    }

    @Override
    public void stop() throws Exception {
        chromeWebDriver.close();
    }

    public WebElement findChatInput() {
        return chromeWebDriver.findElement(CHAT_INPUT.by);
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

    private List<WebElement> getGroupReferences() {
        return chromeWebDriver.findElements(GROUP_REFERENCE.by);
    }

    private void scrollDownPaneSide() throws InterruptedException {
        chromeWebDriver.executeScript("document.getElementById('pane-side').scrollBy({top: 600})");
        Thread.sleep(100);
    }

    private static boolean isStale(WebElement element) {
        try {
            element.isEnabled();
            return false;
        } catch (StaleElementReferenceException ignored) {
            return true;
        }
    }
}
