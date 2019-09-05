package ru.kalmykov.whatsappsender.entity;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Immutable
@ParametersAreNonnullByDefault
public class Message {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm, dd.MM.uuuu", Locale.getDefault());

    public final LocalDateTime dateTime;
    public final String author;
    public final String text;

    public Message(WebElement element) {
        WebElement innerElement = element.findElement(By.xpath(".//div[@data-pre-plain-text]"));
        String data = innerElement.getAttribute("data-pre-plain-text");
        this.dateTime = LocalDateTime.parse(StringUtils.substringBetween(data, "[", "]"), DATE_TIME_FORMATTER);
        this.author = StringUtils.substringBetween(data, "] ", ":");
        this.text = element.findElement(By.xpath(".//span[@dir='ltr']")).getText();
    }

    @Override
    public String toString() {
        return "Message{" +
                "dateTime=" + dateTime +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
