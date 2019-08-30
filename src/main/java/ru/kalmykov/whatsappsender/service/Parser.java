package ru.kalmykov.whatsappsender.service;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static ru.kalmykov.whatsappsender.value.WhatsappItems.GROUP;
import static ru.kalmykov.whatsappsender.value.WhatsappItems.PANE_SIDE;

@Service
@ParametersAreNonnullByDefault
public class Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    private final WebDriver driver;

    public Parser(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getPanel() {
        return driver.findElement(PANE_SIDE.by);
    }

    public List<WebElement> getGroups() {
        return driver.findElements(GROUP.by);
    }

    public void scrollDown() {
        driver.executeScript("document.getElementById('pane-side').scrollBy({top: 600})");
    }

    @Nullable
    public WebElement getGroup(String groupName) throws InterruptedException {
        while (true){
            WebElement group = getGroups()
                    .stream()
                    .filter(e -> groupName.equals(e.getText()))
                    .findFirst()
                    .orElse(null);
            if (group!=null){
                return group;
            }
            scrollDown();
            Thread.sleep(500);
        }
    }

    public static boolean isStale(WebElement element) {
        try {
            element.isEnabled();
            return false;
        } catch (StaleElementReferenceException ignored) {
            return true;
        }
    }
}
