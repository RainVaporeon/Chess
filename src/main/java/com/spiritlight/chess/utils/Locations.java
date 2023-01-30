package com.spiritlight.chess.utils;

public class Locations {
    public static boolean isDiagonal(Location loc1, Location loc2) {
        return Vector.of(loc1, loc2).isDiagonal();
    }

    public static boolean isVertical(Location loc1, Location loc2) {
        return Vector.of(loc1, loc2).isVertical();
    }

    public static boolean isHorizontal(Location loc1, Location loc2) {
        return Vector.of(loc1, loc2).isHorizontal();
    }

    public static Location of(int x, int y) {
        return new Location(x, y);
    }
}
