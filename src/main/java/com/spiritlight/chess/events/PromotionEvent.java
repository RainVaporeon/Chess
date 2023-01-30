package com.spiritlight.chess.events;

import com.spiritlight.chess.pieces.AbstractPiece;
import com.spiritlight.chess.pieces.King;
import com.spiritlight.chess.pieces.Pawn;
import com.spiritlight.chess.utils.Location;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class PromotionEvent extends Event {
    private final AbstractPiece piece;
    private final Location location;
    private final UUID boardID;
    private final AbstractPiece promotion;

    /**
     * Creates a new capture event
     * @param piece The piece that is captured
     * @param location The location of the piece
     * @param boardID The board ID
     */
    public PromotionEvent(AbstractPiece piece, AbstractPiece promotion, Location location, UUID boardID) {
        this.location = location;
        this.piece = piece;
        this.boardID = boardID;
        if(promotion instanceof King || promotion instanceof Pawn) {
            throw new IllegalArgumentException("You may not promote a Pawn to King or itself!");
        }
        this.promotion = promotion;
    }

    public AbstractPiece getPiece() {
        return piece;
    }

    public Location getLocation() {
        return location;
    }

    public UUID getBoardID() {
        return boardID;
    }

    public AbstractPiece getPromotion() {
        return promotion;
    }

    public interface Listener {
        void onPromote(PromotionEvent event);
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
        if(!(e instanceof PromotionEvent pe)) throw new IllegalArgumentException("Cannot fire " + e.getClass().getCanonicalName() + " as PromotionEvent");
        for(Listener listener : listeners) {
            listener.onPromote(pe);
        }
    }
}
