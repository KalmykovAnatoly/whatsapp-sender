package ru.kalmykov.whatsappsender.entity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDateTime;

@ParametersAreNonnullByDefault
public class Message {

    public final String id;
    @Nullable
    public final LocalDateTime dateTime;
    @Nullable
    public final String author;
    @Nullable
    public final String text;

    public Message(
            String id,
            @Nullable LocalDateTime dateTime,
            @Nullable String author,
            @Nullable String text
    ) {
        this.id = id;
        this.dateTime = dateTime;
        this.author = author;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!id.equals(message.id)) return false;
        if (dateTime != null ? !dateTime.equals(message.dateTime) : message.dateTime != null) return false;
        if (author != null ? !author.equals(message.author) : message.author != null) return false;
        return text != null ? text.equals(message.text) : message.text == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", dateTime=" + dateTime +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
