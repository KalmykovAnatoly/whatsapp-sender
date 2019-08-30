package ru.kalmykov.whatsappsender.common.action;

public interface Action {

    Action IDENTITY = () -> {
        //empty
    };

    void execute() throws InterruptedException;
}