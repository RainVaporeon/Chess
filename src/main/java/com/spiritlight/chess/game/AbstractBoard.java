package com.spiritlight.chess.game;

import com.spiritlight.chess.events.CaptureEvent;
import com.spiritlight.chess.events.PromotionEvent;
import com.spiritlight.chess.pieces.AbstractPiece;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Side;

public abstract class AbstractBoard implements CaptureEvent.Listener, PromotionEvent.Listener {
    /**
     * Moves a piece from a location to another
     * @param side The side to make a move
     * @param from The source location
     * @param to The destination location
     * @return {@code true} if the move was allowed and if there is a valid
     * piece on the location, {@code false} otherwise
     */
    protected abstract boolean movePiece(Side side, Location from, Location to);

    public abstract AbstractPiece getPiece(Location source);

    public abstract boolean hasPiece(Location source);

    /**
     * Finalizes the board, this will no longer listen to any events,
     * and references of the board will be removed.
     *
     * @apiNote Even though this implementation automatically removes
     * itself from listening to captures and promotions, one often should
     * be cleaning up the board to free any references from it.
     */
    protected void finish() {
        CaptureEvent.removeListener(this);
        PromotionEvent.removeListener(this);
    }

    /**
     * Method to check whether the location is outside the
     * board boundaries.
     * @param location The location to test
     * @return {@code true} if it's out of bounds, {@code false} otherwise.
     */
    public abstract boolean isOutside(Location location);
}
