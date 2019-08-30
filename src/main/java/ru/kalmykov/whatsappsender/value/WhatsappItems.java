package ru.kalmykov.whatsappsender.value;

import org.openqa.selenium.By;

public enum WhatsappItems {

    PANE_SIDE(By.id("pane-side")),
    GROUP(By.xpath("//span[@dir='auto']"));

    public final By by;

    WhatsappItems(By by) {
        this.by = by;
    }
}
