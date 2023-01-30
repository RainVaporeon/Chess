package com.spiritlight.chess.utils;

public enum Side {
    WHITE,
    BLACK;

    public Side other() {
        return switch(this) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
        };
    }
}
