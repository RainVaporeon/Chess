package com.spiritlight.chess.pieces;

import com.spiritlight.chess.events.CaptureEvent;
import com.spiritlight.chess.game.AbstractBoard;
import com.spiritlight.chess.game.GameBoard;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Side;

import java.util.UUID;

public abstract class AbstractPiece {
    UUID boardID;
    AbstractBoard gameBoard;
    Location location;
    boolean initialMove;
    Side side;

    public AbstractPiece(UUID board, Location location, Side side) {
        this.boardID = board;
        // Attempts to fetch a board from our own implementation.
        // If not possible, the setGameBoard method exists for setting
        // user's custom implementation of such board.
        this.gameBoard = GameBoard.getBoard(board);
        this.location = location;
        this.side = side;
        initialMove = true;
    }

    /**
     * Whether this piece can move to the destination
     * @param destination The destination to move to
     * @return Whether this move is legal
     */
    public abstract boolean canMove(Location destination);

    /**
     * Gets the corresponding character to this piece
     */
    public abstract String character();

    public boolean canCapture(AbstractPiece piece) {
        return piece.allowCapture(this);
    }

    public boolean allowCapture(AbstractPiece source) {
        return true;
    }

    /**
     * Moves this piece.
     * Note that implementations of this should acknowledge that
     * this does not actually move the piece, and this is only
     * to register a move/capture on the piece
     * <p></p>
     * Note that {@link CaptureEvent} is fired
     * @param destination The destination of this piece
     * @return Whether this move was legal
     */
    public boolean move(Location destination) {
        if(!canMove(destination)) return false;
        if(gameBoard.hasPiece(destination) && gameBoard.getPiece(destination).side != this.side) {
            CaptureEvent.fire(new CaptureEvent(gameBoard.getPiece(destination), this, destination, boardID));
        }
        initialMove = false;
        return true;
    }

    public void setGameBoard(AbstractBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Location getLocation() {
        return location;
    }

    public Side getSide() {
        return side;
    }

    public UUID getBoardID() {
        return boardID;
    }

    public void setBoardID(UUID boardID) {
        if(this.boardID != null) throw new IllegalStateException("The board ID has already been initialized!");
        this.boardID = boardID;
    }

    @Override
    public String toString() {
        return "Piece" + side + "#" + boardID + "@" + location.toString();
    }
}
