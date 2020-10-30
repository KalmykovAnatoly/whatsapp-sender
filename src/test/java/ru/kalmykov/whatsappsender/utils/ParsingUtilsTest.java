package ru.kalmykov.whatsappsender.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

class ParsingUtilsTest {

    private static final String TEST_MESSAGE = "<div class=\"_274yw\"><div class=\"copyable-text\" data-pre-plain-text=\"[17:53, 27.10.2020] Бэтмен: \"><div class=\"eRacY\" dir=\"ltr\"><span dir=\"ltr\" class=\"_3Whw5 selectable-text invisible-space copyable-text\"><span>Забавно что именно Балтика стоит 57 рублей</span></span><span class=\"_2oWZe\"></span></div></div><div class=\"_2frDn\"><div class=\"VGBA3\"><span class=\"_18lLQ\" dir=\"auto\">17:53</span></div></div></div><span></span>";

    @Test
    void extractLocalDateTimeNotNull() {
        LocalDateTime localDateTime = ParsingUtils.extractLocalDateTime(TEST_MESSAGE);
        Assert.assertNotNull(localDateTime);
        Assert.assertEquals(17, localDateTime.getHour());
        Assert.assertEquals(53, localDateTime.getMinute());
        Assert.assertEquals(27, localDateTime.getDayOfMonth());
        Assert.assertEquals(Month.OCTOBER, localDateTime.getMonth());
        Assert.assertEquals(2020, localDateTime.getYear());
    }

    @Test
    void extractLocalDateTimeNull() {
        String message = "<div>";
        LocalDateTime localDateTime = ParsingUtils.extractLocalDateTime(message);
        Assert.assertNull(localDateTime);
    }

    @Test
    void extractAuthorNotNull() {
        String author = ParsingUtils.extractAuthor(TEST_MESSAGE);
        Assert.assertEquals("Бэтмен", author);
    }

    @Test
    void extractAuthorNull() {
        String message = "<div>";
        String author = ParsingUtils.extractAuthor(message);
        Assert.assertNull(author);
    }

    @Test
    void extractTextNotNull() {
        String text = ParsingUtils.extractText(TEST_MESSAGE);
        Assert.assertEquals("Забавно что именно Балтика стоит 57 рублей", text);
    }

    @Test
    void extractTextNull() {
        String message = "<div>";
        String text = ParsingUtils.extractText(message);
        Assert.assertNull(text);
    }
}