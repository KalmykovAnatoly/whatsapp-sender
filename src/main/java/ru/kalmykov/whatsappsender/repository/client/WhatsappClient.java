package ru.kalmykov.whatsappsender.repository.client;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.common.lifecycle.Startable;
import ru.kalmykov.whatsappsender.service.WebDriver;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.kalmykov.whatsappsender.value.WhatsappItems.GROUP_REFERENCE;

@Service
@ParametersAreNonnullByDefault
public class WhatsappClient implements Startable {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhatsappClient.class);

    private final static String groupName = "Вов - духовное возвышение";

    private final WebDriver webDriver;
    private final String endpoint;

    public WhatsappClient(
            WebDriver webDriver,
            @Value("${endpoint.whatsapp}") String endpoint
    ) {
        this.webDriver = webDriver;
        this.endpoint = endpoint;
    }

    @Override
    public void start() throws Exception {
        webDriver.get(endpoint);
        WebElement scanMeElement = webDriver.findElement(By.xpath("//img[@alt='Scan me!']"));
        while (!isStale(scanMeElement)) {
            Thread.sleep(2000);
            LOGGER.debug("WAITING FOR SCAN");
        }
        LOGGER.debug("SCANNED");
    }

    @Override
    public void stop() throws Exception {

    }

    @Nullable
    public WebElement findGroupReference() throws InterruptedException {
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
            LOGGER.debug("" + currentGroups.size() + " GROUPS ADDED");
            scrollDownPaneSide();
        }
        return groupReference;
    }

    private List<WebElement> getGroupReferences() {
        return webDriver.findElements(GROUP_REFERENCE.by);
    }

    private void scrollDownPaneSide() throws InterruptedException {
        webDriver.executeScript("document.getElementById('pane-side').scrollBy({top: 600})");
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
