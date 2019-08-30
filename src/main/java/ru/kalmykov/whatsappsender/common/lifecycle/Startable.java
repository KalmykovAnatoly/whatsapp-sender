package ru.kalmykov.whatsappsender.common.lifecycle;

public interface Startable {

    void start() throws Exception;

    void stop() throws Exception;
}