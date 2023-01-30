package com.spiritlight.chess.utils;

import com.spiritlight.chess.game.GameBoard;

/**
 * A simple formatter that, when supplied a string,
 * outputs a formatted string.
 */
@FunctionalInterface
public interface Formatter {
    String format(String input);

    /**
     * Default formatting: Does not format at all, as {@link GameBoard#getBoardView()}
     * has already set that part up for us.
     * @return A formatter that returns whatever is supplied to it.
     */
    static Formatter getDefaultFormatting() {
        return input -> input;
    }
}
