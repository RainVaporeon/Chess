package com.spiritlight.chess.events;

import com.spiritlight.chess.pieces.AbstractPiece;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Side;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaptureEvent extends Event {
    private final AbstractPiece piece;
    private final AbstractPiece capturingPiece;
    private final Side capturingSide;
    private final Location location;
    private final UUID boardID;

    /**
     * Creates a new capture event
     * @param piece The piece that is captured
     * @param location The location of the piece
     * @param boardID The board ID
     */
    public CaptureEvent(AbstractPiece piece, AbstractPiece capturingPiece, Location location, UUID boardID) {
        this.location = location;
        this.capturingPiece = capturingPiece;
        this.piece = piece;
        this.boardID = boardID;
        this.capturingSide = capturingPiece.getSide();
    }

    /**
     * Gets the captured piece
     */
    public AbstractPiece getPiece() {
        return piece;
    }

    public Location getLocation() {
        return location;
    }

    public UUID getBoardID() {
        return boardID;
    }

    public Side getCapturingSide() {
        return capturingSide;
    }

    public AbstractPiece getCapturingPiece() {
        return capturingPiece;
    }

    public interface Listener {
        void onCapture(CaptureEvent event);
    }

    // Thread safety in case of future multi-board uses
    private static final List<Listener> listeners = new CopyOnWriteArrayList<>();

    public static boolean addListener(Object o) {
        if(!(o instanceof Listener listener)) return false;
        return listeners.add(listener);
    }

    public static boolean removeListener(Object o) {
        if(!(o instanceof Listener listener)) return false;
        return listeners.remove(listener);
    }

    public static void fire(Event e) {
        if(!(e instanceof CaptureEvent ce)) throw new IllegalArgumentException("Cannot fire " + e.getClass().getCanonicalName() + " as CaptureEvent");
        for(Listener listener : listeners) {
            listener.onCapture(ce);
        }
    }
}
