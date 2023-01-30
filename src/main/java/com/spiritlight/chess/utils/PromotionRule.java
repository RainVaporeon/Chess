package com.spiritlight.chess.utils;

public enum PromotionRule {
    /**
     * Promotion applies to pawns
     */
    STANDARD,
    /**
     * Promotion applies half-way into the board
     */
    HALFWAY,
    /**
     * Promotion does not occur
     */
    DISABLED,


}
