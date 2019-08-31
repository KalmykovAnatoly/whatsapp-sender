package ru.kalmykov.whatsappsender.value;

import org.openqa.selenium.By;

public enum WhatsappItems {

    PANE_SIDE(By.id("pane-side")),
    GROUP_REFERENCE(By.xpath("//span[@dir='auto']")),
    CHAT_INPUT(By.xpath("//div[contains(@class, '_13mgZ')]"));

    public final By by;

    WhatsappItems(By by) {
        this.by = by;
    }
}
