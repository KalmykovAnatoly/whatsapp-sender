package ru.kalmykov.whatsappsender.entity;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

@Immutable
@ParametersAreNonnullByDefault
public class Emoji {

    private final byte sheet;
    private final byte x;
    private final byte y;

    public Emoji(byte sheet, byte x, byte y) {
        this.sheet = sheet;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Emoji{" +
                "sheet=" + sheet +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
