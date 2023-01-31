package com.spiritlight.chess.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Location extends Coordinates {

    public Location(int x, int y) {
        super(x, y);
    }

    public Location(char x, int y) {
        this(fromReflectedAddress(x), y);
    }

    // Minimum lowercase char value 'a'
    private static final char MIN_LOWER = 'a';
    // Minimum uppercase char value 'A'
    private static final char MIN_UPPER = 'A';
    // Length of alphabets existing
    private static final short LENGTH = 26;
    public Location(char x, char y) {
        if(Character.isDigit(x)) {
            try {
                this.setX(Integer.parseInt(Character.toString(x)));
                this.setY(y);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid location ID: " + x + "" + y);
            }
            return;
        }
        if(!Character.isAlphabetic(x)) throw new IllegalArgumentException("Invalid location ID: " + x + "" + y);
        if(Character.isLowerCase(x)) {
            this.setX(x - MIN_LOWER);
            this.setY(y);
        } else {
            this.setX(x - MIN_UPPER + LENGTH);
            this.setY(y);
        }
    }

    @Contract("_, _ -> new")
    public static @NotNull Location of(int x, int y) {
        return new Location(x, y);
    }

    @Contract("_, _ -> new")
    public static @NotNull Location of(char x, int y) {
        return new Location(x, y);
    }

    @Contract("_, _ -> new")
    public static @NotNull Location of(char x, char y) {
        return new Location(x, y);
    }

    @Contract("_, _ -> new")
    public static @NotNull Location of(@NotNull Location source, @NotNull Vector vector) {
        return new Location(source.x() + vector.x(), source.y() + vector.y());
    }

    /**
     * Applies a given vector to this object
     * @param vector The vector to apply in
     * @return A new Location object after applying this vector
     */
    @Contract("_ -> new")
    public Location apply(Vector vector) {
        if(vector == null) {
            throw new NullPointerException("vector is null");
        }
        return of(this, vector);
    }

    public int x() {
        return this.getX();
    }

    public int y() {
        return this.getY();
    }

    /**
     *
     * @param c The <i>character</i> to parse to an integer address
     * @return The integer address this character is mapped to
     */
    public static int fromReflectedAddress(char c) {
        if(!Character.isAlphabetic(c)) return -1;
        if(Character.isLowerCase(c)) {
            return c - MIN_LOWER;
        } else {
            return c - MIN_UPPER + LENGTH;
        }
    }

    /**
     *
     * @param i The integer address to parse to a character
     * @return The character this integer is mapped to
     * @throws IllegalArgumentException if i is larger than two times {@link Location#LENGTH}, or less than 0
     */
    @SuppressWarnings("GrazieInspection")
    public static char toReflectedAddress(int i) {
        if(i > LENGTH * 2 || i < 0) throw new IllegalArgumentException("i cannot be larger than " + LENGTH * 2 + " or less than 0");
        if(i <= LENGTH) {
            return (char) (MIN_LOWER + i);
        } else {
            return (char) (MIN_UPPER + i);
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Location@" + toReflectedAddress(this.getX()) + "" + this.getY();
    }
}
