package com.spiritlight.chess.utils;

import java.util.Objects;

import static java.lang.Math.abs;

public final class Vector extends Coordinates {
    private final int x;
    private final int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return x == vector.x && y == vector.y;
    }

    /**
     * Compares two vectors with the absolute value instead of
     * the actual parameter value
     *
     * @param that The vector to compare with
     * @return true if and only if the absolute value of x and y is equal
     */
    public boolean absoluteEquals(Vector that) {
        return abs(this.x) == abs(that.x) && abs(this.y) == abs(that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public String toString() {
        return "Vector[" +
                "x=" + x + ", " +
                "y=" + y + ']';
    }

}
