package com.spiritlight.chess.pieces;

import com.spiritlight.chess.events.CaptureEvent;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Locations;
import com.spiritlight.chess.utils.Side;
import com.spiritlight.chess.utils.Vector;
import org.jetbrains.annotations.NotNull;

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
    protected boolean isBlocked(@NotNull Vector vector) {
        if(!vector.isDiagonal()) return false;
        if(vector.absoluteEquals(Vector.of(1, 1))) return false;
        boolean xPos = vector.x() > 0;
        boolean yPos = vector.y() > 0;
        for(int i = 2; i < Math.abs(vector.x()); i++) {
            if(gameBoard.hasPiece(location.apply(Vector.of(xPos ? i : -i, yPos ? i : -i)))) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Bishop" + super.toString();
    }
}
