package ru.kalmykov.whatsappsender.util;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class StateMachine {

    public String parse(String text) {
        StringBuilder builder = new StringBuilder();
        Character character;
        for (char symbol : text.toCharArray()) {
            switch (symbol) {
                case '<':
                    character = State.state.handleOpenBracket(symbol);
                    break;
                case '>':
                    character = State.state.handleCloseBracket(symbol);
                    break;
                default:
                    character = State.state.handleLetter(symbol);
            }
            if (character != null) {
                builder.append(character);
            }
        }
        return builder.toString();
    }

    private enum State {

        START {
            @Nullable
            @Override
            public Character handleOpenBracket(char symbol) {
                state = OPENED;
                return null;
            }
        },
        OPENED {
            @Nullable
            @Override
            public Character handleCloseBracket(char symbol) {
                state = CLOSED;
                return null;
            }
        },
        CLOSED {
            @Nullable
            @Override
            public Character handleOpenBracket(char symbol) {
                state = OPENED;
                return null;
            }

            @Override
            public Character handleLetter(char symbol) {
                return symbol;
            }
        };

        public static State state = START;

        @Nullable
        public Character handleOpenBracket(char symbol) {
            return null;
        }

        @Nullable
        public Character handleCloseBracket(char symbol) {
            return null;
        }

        @Nullable
        public Character handleLetter(char symbol) {
            return null;
        }
    }
}
