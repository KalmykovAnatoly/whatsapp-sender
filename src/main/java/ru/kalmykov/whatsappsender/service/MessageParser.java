package ru.kalmykov.whatsappsender.service;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kalmykov.whatsappsender.entity.Message;
import ru.kalmykov.whatsappsender.utils.ParsingUtils;

import javax.annotation.ParametersAreNonnullByDefault;

@Component
@ParametersAreNonnullByDefault
public class MessageParser {

    public Message parseMessage(WebElement element) {
        String html = element.getAttribute("innerHTML");
        return new Message(
                ParsingUtils.extractLocalDateTime(html),
                ParsingUtils.extractAuthor(html),
                ParsingUtils.extractText(html)
        );
    }
}
