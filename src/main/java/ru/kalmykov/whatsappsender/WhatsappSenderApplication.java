package ru.kalmykov.whatsappsender;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEncryptableProperties
@EnableScheduling
@SpringBootApplication
public class WhatsappSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsappSenderApplication.class, args);
    }
}
