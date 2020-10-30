package ru.kalmykov.whatsappsender.value;

import org.openqa.selenium.By;

public enum Xpath {

    //todo xpath to config
    CHAT_INPUT("//*[@spellcheck='true']"),
    CHAT_OUTPUT("//div[contains(@class, '_1_q7u')]"),
    CURRENT_GROUP_HEADER("//*[@*='DP7CM']"),
    GROUP_REFERENCE("//div[contains(@class, '_210SC')]"),
    LOADING("//div[contains(@class, '_2sOZc')]"),
    MESSAGE("//*[@data-id]"),
    SCROLL_BUTTON("//div[contains(@class, '_3KRbU')]"),
    SEND_BUTTON("//span[@data-icon='send']"),
    SEARCH_BAR("//button[contains(@class, '_1XCAr')]");


    public final String xpath;

    public static By of(Xpath item) {
        return By.xpath(item.xpath);
    }

    Xpath(String xpath) {
        this.xpath = xpath;
    }
}
