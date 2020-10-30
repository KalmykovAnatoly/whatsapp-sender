package ru.kalmykov.whatsappsender.repository.client;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.LocalStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.service.ChromeWebDriver;
import ru.kalmykov.whatsappsender.value.Xpath;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.kalmykov.whatsappsender.value.Xpath.*;

@Service
@ParametersAreNonnullByDefault
public class WhatsappClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhatsappClient.class);

    private final ChromeWebDriver chromeWebDriver;
    private final String endpoint;
    private final String waBrowserId;
    private final String waToken1;
    private final String waToken2;
    private final String waSecretBundle;

    public WhatsappClient(
            ChromeWebDriver chromeWebDriver,
            @Value("${whatsapp-client.endpoint}") String endpoint,
            @Value("${whatsapp-client.waBrowserId}") String waBrowserId,
            @Value("${whatsapp-client.waToken1}") String waToken1,
            @Value("${whatsapp-client.waToken2}") String waToken2,
            @Value("${whatsapp-client.waSecretBundle}") String waSecretBundle
    ) {
        this.chromeWebDriver = chromeWebDriver;
        this.endpoint = endpoint;
        this.waBrowserId = waBrowserId;
        this.waToken1 = waToken1;
        this.waToken2 = waToken2;
        this.waSecretBundle = waSecretBundle;
    }

    public void enterWhatsapp() {
        chromeWebDriver.get(endpoint);
        LocalStorage local = chromeWebDriver.getLocalStorage();
        local.setItem("WABrowserId", waBrowserId);
        local.setItem("WAToken1", waToken1);
        local.setItem("WAToken2", waToken2);
        local.setItem("WASecretBundle", waSecretBundle);
        chromeWebDriver.navigate().refresh();
        chromeWebDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public void refresh() {
        chromeWebDriver.navigate().refresh();
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
    public WebElement findSearchBar() {
        return safeFinding(SEARCH_BAR);
    }

    public WebElement findLoading() {
        return safeFinding(LOADING);
    }

    public void scrollUpChatOutput(int number) {
        chromeWebDriver.executeScript("document.getElementsByClassName('_1_keJ')[0].scrollBy({top: -" + number + "})");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scrollDownPaneSide(int number) {
        chromeWebDriver.executeScript("document.getElementById('pane-side').scrollBy({top: " + number + "})");
    }

    public List<WebElement> getGroupReferences() {
        return chromeWebDriver.findElements(Xpath.of(GROUP_REFERENCE));
    }

    public List<WebElement> getMessages() {
        return chromeWebDriver.findElements(Xpath.of(MESSAGE));
    }

    public static boolean isStale(WebElement element) {
        try {
            element.isEnabled();
            return false;
        } catch (StaleElementReferenceException ignored) {
            return true;
        }
    }

    @Nullable
    private WebElement safeFinding(Xpath item) {
        try {
            return chromeWebDriver.findElement(Xpath.of(item));
        } catch (Exception e) {
            LOGGER.error("ELEMENT NOT FOUND: " + item.name());
            return null;
        }
    }
}
