package com.spiritlight.chess.utils;

public record Vector(int x, int y) {
    public static Vector of(int x, int y) {
        return new Vector(x, y);
    }

    public static Vector of(Location l1, Location l2) {
        return new Vector(l2.getX() - l1.getX(), l2.getY() - l1.getY());
    }

    public boolean isDiagonal() {
        return x == y;
    }

    public boolean isVertical() {
        return x == 0 && y != 0;
    }

    public boolean isHorizontal() {
        return x != 0 && y == 0;
    }
}
