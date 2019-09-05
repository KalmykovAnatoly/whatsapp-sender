package ru.kalmykov.whatsappsender.entity;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import ru.kalmykov.whatsappsender.Exception.NotFoundException;

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

    private final LocalDateTime dateTime;
    private String author;
    private String text;

    public Message(LocalDateTime dateTime, String author, String text) {
        this.dateTime = dateTime;
        this.author = author;
        this.text = text;
    }

    public static void main(String[] args) {
        String test = "11:27, 04.09.2019";
        System.out.println(LocalDateTime.parse(test, DATE_TIME_FORMATTER));
    }

    @Override
    public String toString() {
        return "Message{" +
                "dateTime=" + dateTime +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    private static LocalDateTime extractDateTime(WebElement webElement) {
        String date = webElement.getAttribute("data-pre-plain-text");
        String dateTime = StringUtils.substringBetween(date, "[", "]");
        if (dateTime == null) {
            throw new NotFoundException("#DATE.TIME.NOT.FOUND; DateTime не найден");
        }
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }
}
