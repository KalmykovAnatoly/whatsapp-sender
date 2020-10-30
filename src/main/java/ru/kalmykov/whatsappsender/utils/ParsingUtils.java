package ru.kalmykov.whatsappsender.utils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ParametersAreNonnullByDefault
public class ParsingUtils {

    private static final Pattern DATE = Pattern.compile("(\\d{2}:\\d{2}, \\d{2}\\.\\d{2}\\.\\d{4})");
    private static final Pattern NAME = Pattern.compile("data-pre-plain-text=\"\\[.*?] (.*?): \"><div class=");
    private static final Pattern TEXT = Pattern.compile("<span>(.+?)</span>");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.uuuu");

    @Nullable
    public static LocalDateTime extractLocalDateTime(String html) {
        Matcher matcher = DATE.matcher(html);
        if (matcher.find()) {
            return LocalDateTime.parse(matcher.group(), DATE_TIME_FORMATTER);
        }else{
            return null;
        }
    }

    @Nullable
    public static String extractAuthor(String html) {
        Matcher matcher = NAME.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }else{
            return null;
        }
    }

    @Nullable
    public static String extractText(String html) {
        Matcher matcher = TEXT.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }else{
            return null;
        }
    }
}
