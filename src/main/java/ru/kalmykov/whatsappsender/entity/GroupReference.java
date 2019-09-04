package ru.kalmykov.whatsappsender.entity;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import ru.kalmykov.whatsappsender.Exception.NotFoundException;

public class GroupReference {

    public final int position;
    public final String title;
    public final WebElement webElement;

    public GroupReference(WebElement webElement) {
        this.webElement = webElement;
        this.position = extractPosition(webElement);
        this.title = extractTitle(webElement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupReference that = (GroupReference) o;

        if (position != that.position) return false;
        if (!title.equals(that.title)) return false;
        return webElement.equals(that.webElement);
    }

    @Override
    public int hashCode() {
        int result = position;
        result = 31 * result + title.hashCode();
        result = 31 * result + webElement.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GroupReference{" +
                "position=" + position +
                ", title='" + title + '\'' +
                '}';
    }

    private static int extractPosition(WebElement webElement) {
        String style = webElement.getAttribute("style");
        String translation = StringUtils.substringBetween(style, "translateY(", "px)");
        if (translation == null) {
            throw new NotFoundException("#TRANSLATION.NOT.FOUND; Translation не найдено");
        }
        return Integer.valueOf(translation) / 72;
    }

    private static String extractTitle(WebElement webElement) {
        String[] lines = webElement.getText().split("\\r?\\n");
        if (lines.length > 0) {
            return lines[0];
        } else return "";
    }
}
