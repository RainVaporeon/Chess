package com.spiritlight.chess.pieces;

import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Side;
import com.spiritlight.chess.utils.Vector;

import java.util.UUID;
import static java.lang.Math.abs;

public class Knight extends AbstractPiece {
    public Knight(UUID board, Location location, Side side) {
        super(board, location, side);
    }

    @Override
    public boolean canMove(Location destination) {
        if(gameBoard.isOutside(destination)) return false;
        Vector move = Vector.of(location, destination);
        int diff = abs(move.x()) - abs(move.y());
        if(diff != 1) return false;
        return abs(move.x()) <= 2 && abs(move.y()) <= 2;
    }

    @Override
    public String character() {
        return side == Side.WHITE ? "♘" : "♞";
    }

    @Override
    public String toString() {
        return "Knight" + super.toString();
    }
}
