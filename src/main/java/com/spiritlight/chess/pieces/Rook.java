package com.spiritlight.chess.pieces;

import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Side;
import com.spiritlight.chess.utils.Vector;

import java.util.UUID;

public class Rook extends AbstractPiece {
    public Rook(UUID board, Location location, Side side) {
        super(board, location, side);
    }

    @Override
    public boolean canMove(Location destination) {
        if(this.gameBoard.isOutside(destination)) return false;
        Vector move = Vector.of(location, destination);
        if(move.isDiagonal()) return false;
        return move.isHorizontal() || move.isVertical();
    }

    @Override
    public String character() {
        return side == Side.WHITE ? "♖" : "♜";
    }

    // TODO implement this
    @Override
    protected boolean isBlocked(Vector vector) {
        return false;
    }

    @Override
    public String toString() {
        return "Rook" + super.toString();
    }
}
