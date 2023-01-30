package com.spiritlight.chess.events;

import com.spiritlight.chess.game.AbstractBoard;
import com.spiritlight.chess.pieces.AbstractPiece;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Side;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameFinishEvent extends GameEvent {
    private final Side winner;

    /**
     * Creates a new game event
     *
     * @param board  The board involved
     * @param layout The current game layout
     */
    public GameFinishEvent(AbstractBoard board, Map<Location, AbstractPiece> layout, Side winner) {
        super(board, layout);
        this.winner = winner;
    }

    public Side getWinner() {
        return winner;
    }

    public interface Listener {
        void onFinish(GameFinishEvent e);
    }

    private static final List<GameFinishEvent.Listener> listeners = new CopyOnWriteArrayList<>();

    public static boolean addListener(Object o) {
        if(!(o instanceof GameFinishEvent.Listener listener)) return false;
        return listeners.add(listener);
    }

    public static boolean removeListener(Object o) {
        if(!(o instanceof GameFinishEvent.Listener listener)) return false;
        return listeners.remove(listener);
    }

    public static void fire(Event e) {
        if(!(e instanceof GameFinishEvent gfe)) throw new IllegalArgumentException("Cannot fire " + e.getClass().getCanonicalName() + " as GameFinishEvent");
        for(GameFinishEvent.Listener listener : listeners) {
            listener.onFinish(gfe);
        }
    }
}
