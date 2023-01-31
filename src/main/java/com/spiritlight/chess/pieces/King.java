package com.spiritlight.chess.pieces;

import com.spiritlight.chess.events.CaptureEvent;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Side;
import com.spiritlight.chess.utils.Vector;

import java.util.UUID;
import static java.lang.Math.abs;

public class King extends AbstractPiece {

    public King(UUID board, Location location, Side side) {
        super(board, location, side);
    }

    @Override
    public boolean canMove(Location destination) {
        if(gameBoard.isOutside(destination) || isBlocked(destination)) return false;
        int x = location.x() - destination.x();
        int y = location.y() - destination.y();
        return abs(x) == 1 || abs(y) == 1;
    }

    @Override
    public String character() {
        return side == Side.WHITE ?  "♔" : "♚";
    }

    @Override
    protected boolean isBlocked(Vector vector) {
        return !gameBoard.hasPiece(location.apply(vector)) || gameBoard.getPiece(location.apply(vector)).side == this.side;
    }

    @Override
    public String toString() {
        return "King" + super.toString();
    }
}
