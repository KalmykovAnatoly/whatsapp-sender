package ru.kalmykov.whatsappsender.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kalmykov.whatsappsender.common.lifecycle.Startable;

import javax.annotation.ParametersAreNonnullByDefault;

import static ru.kalmykov.whatsappsender.service.Parser.isStale;

@Service
@ParametersAreNonnullByDefault
public class WhatsappStarter implements Startable {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhatsappStarter.class);

    private final WebDriver webDriver;
    private final String endpoint;

    public WhatsappStarter(
            WebDriver webDriver,
            @Value("${endpoint.whatsapp}") String endpoint
    ) {
        this.webDriver = webDriver;
        this.endpoint = endpoint;
    }

    @Override
    public void start() throws Exception {
        webDriver.get(endpoint);
        WebElement scanMeElement = webDriver.findElement(By.xpath("//img[@alt='Scan me!']"));
        while (!isStale(scanMeElement)) {
            Thread.sleep(2000);
            LOGGER.debug("WAITING FOR SCAN");
        }
        LOGGER.debug("SCANNED");
    }

    @Override
    public void stop() throws Exception {

    }
}
