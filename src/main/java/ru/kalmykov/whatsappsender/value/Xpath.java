package ru.kalmykov.whatsappsender.value;

import org.openqa.selenium.By;

public enum Xpath {

    CHAT_INPUT("//div[contains(@class, '_13mgZ')]"),
    CHAT_OUTPUT("//div[contains(@class, '_1_q7u')]"),
    CURRENT_GROUP_HEADER("//div[contains(@class, '_19vo_')]"),
    GROUP_REFERENCE("//span[@dir='auto']"),
    PANE_SIDE("pane-side"),
    SCAN_ME("//img[@alt='Scan me!']"),
    SCROLL_BUTTON("//div[contains(@class, '_3KRbU')]"),
    SEND_BUTTON("//span[@data-icon='send']");

    public final String xpath;

    public static By of(Xpath item) {
        return By.xpath(item.xpath);
    }

    Xpath(String xpath) {
        this.xpath = xpath;
    }
}
