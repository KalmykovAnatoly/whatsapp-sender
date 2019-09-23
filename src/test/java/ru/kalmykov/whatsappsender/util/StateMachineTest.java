package ru.kalmykov.whatsappsender.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class StateMachineTest {

    @Test
    void parse() {
        StateMachine stateMachine = new StateMachine();
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> System.out.println(stateMachine.parse("<sdfsdf>TEST<sadfg>")));
        }
    }
}