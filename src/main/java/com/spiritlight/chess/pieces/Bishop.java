package com.spiritlight.chess.pieces;

import com.spiritlight.chess.events.CaptureEvent;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Locations;
import com.spiritlight.chess.utils.Side;

import java.util.UUID;

public class Bishop extends AbstractPiece {
    public Bishop(UUID board, Location location, Side side) {
        super(board, location, side);
    }

    @Override
    public boolean canMove(Location destination) {
        if(!Locations.isDiagonal(location, destination)) return false;
        return !gameBoard.isOutside(destination);
    }

    @Override
    public String character() {
        return side == Side.WHITE ? "♗" : "♝";
    }

    @Override
    public String toString() {
        return "Bishop" + super.toString();
    }
}
