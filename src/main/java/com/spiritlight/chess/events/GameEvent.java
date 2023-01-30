package com.spiritlight.chess.events;

import com.spiritlight.chess.game.AbstractBoard;
import com.spiritlight.chess.pieces.AbstractPiece;
import com.spiritlight.chess.utils.Location;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameEvent extends Event {
    protected final AbstractBoard board;
    protected final Map<Location, AbstractPiece> layout;

    /**
     * Creates a new game event
     * @param board The board involved
     * @param layout The current game layout
     */
    public GameEvent(AbstractBoard board, Map<Location, AbstractPiece> layout) {
        this.board = board;
        this.layout = layout;
    }

    public AbstractBoard getBoard() {
        return board;
    }

    /**
     * Returns a view-only layout of the current pattern
     * @return An immutable map describing the current game layout
     */
    public Map<Location, AbstractPiece> getLayout() {
        return Collections.unmodifiableMap(layout);
    }

    public interface Listener {
        void onUpdate(GameEvent event);
    }

    // Thread safety in case of future multi-board uses
    private static final List<GameEvent.Listener> listeners = new CopyOnWriteArrayList<>();

    public static boolean addListener(Object o) {
        if(!(o instanceof GameEvent.Listener listener)) return false;
        return listeners.add(listener);
    }

    public static boolean removeListener(Object o) {
        if(!(o instanceof GameEvent.Listener listener)) return false;
        return listeners.remove(listener);
    }

    public static void fire(Event e) {
        if(!(e instanceof GameEvent ge)) throw new IllegalArgumentException("Cannot fire " + e.getClass().getCanonicalName() + " as GameEvent");
        for(GameEvent.Listener listener : listeners) {
            listener.onUpdate(ge);
        }
    }
}
