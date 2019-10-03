package ru.kalmykov.whatsappsender.entity;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import java.time.LocalDateTime;

@Immutable
@ParametersAreNonnullByDefault
public class Message {

    public final LocalDateTime dateTime;
    public final String author;
    public final String text;

    public Message(
            LocalDateTime dateTime,
            String author,
            String text
    ) {
        this.dateTime = dateTime;
        this.author = author;
        this.text = text;
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
