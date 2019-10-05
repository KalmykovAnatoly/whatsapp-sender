package ru.kalmykov.whatsappsender.service;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kalmykov.whatsappsender.entity.Message;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@ParametersAreNonnullByDefault
public class MessageParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageParser.class);

    private static final QName SRC = QName.valueOf("src");
    private static final QName CLASS = QName.valueOf("class");
    private static final QName HREF = QName.valueOf("href");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "HH:mm, dd.MM.uuuu",
            Locale.getDefault()
    );

    public Message parseMessage(WebElement element) {
        WebElement innerElement = element.findElement(By.xpath(".//div[@data-pre-plain-text]"));
        String data = innerElement.getAttribute("data-pre-plain-text");
        LocalDateTime dateTime =
                LocalDateTime.parse(StringUtils.substringBetween(data, "[", "]"), DATE_TIME_FORMATTER);
        String author = StringUtils.substringBetween(data, "] ", ":");
        String text;
        try {
            text = decompose(innerElement.getAttribute("innerHTML"));
        } catch (IOException e) {
            LOGGER.warn("MESSAGE CAN'T BE PARSED FOR AUTHOR: {}, DATE_TIME: {}", author, dateTime);
            text = "";
        }
        return new Message(
                dateTime,
                author,
                text
        );
    }

    private static String decompose(String composedText) throws IOException {
        composedText = composedText.replaceAll("</.*?>", "") + "<>";
        LOGGER.info("TRYING TO DECOMPOSE: " + composedText);
        byte[] byteArray = composedText.getBytes(StandardCharsets.UTF_8);

        StringBuilder builder = new StringBuilder();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray)) {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = inputFactory.createXMLEventReader(inputStream);

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isCharacters()) {
                    builder.append(event.asCharacters());
                    builder.append(" ");
                }
                if (event.isStartElement()) {
                    Attribute src = event.asStartElement().getAttributeByName(SRC);
                    if (src != null) {
                        builder.append(src.getValue());
                        builder.append(" ");
                    }
                }
            }
        } catch (XMLStreamException ignored) {
//            innerHtml crops down closing tags, ok
        }
        return builder.toString();
    }
}
