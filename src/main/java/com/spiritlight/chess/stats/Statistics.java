package com.spiritlight.chess.stats;

import com.spiritlight.chess.pieces.AbstractPiece;
import com.spiritlight.chess.utils.Side;

import java.util.Map;

public record Statistics(Side side, Map<AbstractPiece, Integer> piecesTaken) {
}
