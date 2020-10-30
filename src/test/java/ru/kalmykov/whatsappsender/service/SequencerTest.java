package ru.kalmykov.whatsappsender.service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class SequencerTest {

    @Test
    void filterOld1() {
        List<String> oldChars = Arrays.asList("T", "E", "S", "T", "E", "S", "T", "O");
        List<String> newChars = Arrays.asList("E", "S", "T", "O", "R");
        List<String> remainder = Sequencer.filterOld(oldChars, newChars);
        System.out.println(remainder);
    }

    @Test
    void filterOld2() {
        List<String> oldChars = Arrays.asList("T");
        List<String> newChars = Arrays.asList("E", "S", "T", "O", "R");
        List<String> remainder = Sequencer.filterOld(oldChars, newChars);
        System.out.println(remainder);
    }

    @Test
    void filterOld3() {
        List<String> oldChars = Arrays.asList("T", "E", "S", "T", "E", "S", "T", "O");
        List<String> newChars = Arrays.asList("T", "E", "S", "T");
        List<String> remainder = Sequencer.filterOld(oldChars, newChars);
        System.out.println(remainder);
    }
}