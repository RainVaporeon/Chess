package com.spiritlight.chess.pieces;

import com.spiritlight.chess.events.CaptureEvent;
import com.spiritlight.chess.events.PromotionEvent;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Side;
import com.spiritlight.chess.utils.Vector;

import java.util.UUID;

public class Pawn extends AbstractPiece {
    public Pawn(UUID board, Location location, Side side) {
        super(board, location, side);
    }

    @Override
    public boolean canMove(Location destination) {
        if(destination.equals(location)) return false;
        if(gameBoard.isOutside(destination)) return false;
        Vector move = Vector.of(location, destination);
        if(move.isDiagonal() && gameBoard.hasPiece(destination) && gameBoard.getPiece(destination).side != this.side && move.x() == 1) {
            return true;
        }
        if(move.isHorizontal()) { // Inferred no forward movement is made
            return false;
        }
        int forwardDistance = destination.y() - location.y();
        return forwardDistance <= ((side == Side.WHITE ? 1 : -1) * (initialMove ? 2 : 1)) && move.isVertical();
    }

    @Override
    public boolean move(Location destination) {
        boolean ret = super.move(destination);
        if(!ret) return false;
        tryPromote();
        return true;
    }

    /**
     * Makes an attempt to promote each time it moves.
     */
    private void tryPromote() {
        gameBoard.onPromote(new PromotionEvent(this,
                new Queen(this.boardID, this.location, this.side),
                this.location,
                this.boardID));
    }

    @Override
    public String character() {
        return side == Side.WHITE ? "♙" : "♟";
    }

    @Override
    protected boolean isBlocked(Vector vector) {
        return !vector.isDiagonal() && gameBoard.hasPiece(location.apply(vector));
    }

    @Override
    public String toString() {
        return "Pawn" + super.toString();
    }
}
