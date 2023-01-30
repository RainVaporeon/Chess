package com.spiritlight.chess.game;

import com.spiritlight.chess.events.CaptureEvent;
import com.spiritlight.chess.events.GameEvent;
import com.spiritlight.chess.events.GameFinishEvent;
import com.spiritlight.chess.events.PromotionEvent;
import com.spiritlight.chess.pieces.AbstractPiece;
import com.spiritlight.chess.pieces.King;
import com.spiritlight.chess.pieces.Pawn;
import com.spiritlight.chess.utils.Formatter;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.PromotionRule;
import com.spiritlight.chess.utils.Side;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The main class for a chess game to take place
 * <br /> <br />
 * Note that even though it is possible to initialize a board size that
 * does not cover all pieces loaded in pieceMap, one may still be allowed
 * to do moves from outside the map to inside the board.
 * <p>
 * Additionally, this board implementation also allows multiple boards to exist
 * at once, however, the backing storage is not thread-safe.
 * <p><p>
 * All board are recognized with their special id, backed by a UUID usually.
 * You may retrieve a board by {@link GameBoard#getBoard(UUID)}. If {@code null} is
 * returned, it indicates that the game has finished on the board.
 */
public final class GameBoard extends AbstractBoard {
    private final int x;
    private final int y;
    private final UUID id;
    /**
     * Current board pieces
     */
    private final Map<Location, AbstractPiece> pieceMap;
    /**
     * Promotion rule for this game instance
     */
    private PromotionRule promotionRule;
    /**
     * Whether this board can be modified via {@link Board}'s initializations
     * provided by this class
     */
    private boolean update = true;
    private boolean completed = false;
    private static final List<GameBoard> activeGameBoards = new ArrayList<>();

    @Contract(pure = true)
    public static @Nullable GameBoard getBoard(UUID id) {
        for(GameBoard gameBoard : activeGameBoards) {
            if(gameBoard.id.equals(id)) return gameBoard;
        }
        return null;
    }

    public GameBoard(int x, int y) {
        this(x, y, UUID.randomUUID(), new HashMap<>(), PromotionRule.STANDARD);
    }

    GameBoard(int x, int y, UUID id, Map<Location, AbstractPiece> pieceMap, PromotionRule promotionRule) {
        if(x < 0 || y < 0) throw new IllegalArgumentException("Unable to construct a board with height or width less than 0");
        this.x = x;
        this.y = y;
        this.id = id;
        this.pieceMap = pieceMap;
        this.promotionRule = promotionRule;
        activeGameBoards.add(this);
        CaptureEvent.addListener(this);
        PromotionEvent.addListener(this);
    }

    /**
     * Retrieves the location(s) of a specified piece type
     * @param piece The piece type to look for
     * @return A possibly-empty list of locations of pieces.
     */
    @NotNull
    public List<Location> getLocationOf(AbstractPiece piece) {
        List<Location> ret = new ArrayList<>();
        for(Map.Entry<Location, AbstractPiece> entry : pieceMap.entrySet()) {
            if(entry.getValue().getClass() == piece.getClass()) {
                ret.add(entry.getKey());
            }
        }
        return ret;
    }

    /**
     * Moves the piece
     * @param side The side to make a move
     * @param source The source location
     * @param destination The destination location
     * @return Whether the move succeeded
     */
    @Override
    public boolean movePiece(Side side, Location source, Location destination) {
        if(!hasPiece(source)) {
            System.out.println("No piece");
            return false;
        }
        AbstractPiece piece = getPiece(source);
        if(!piece.canMove(destination) || piece.getSide() != side) {
            System.out.println("Move illegal or wrong side");
            return false;
        }
        piece.move(destination); // Deferring return to fire updated event
        this.updatePieces(source, destination);
        GameEvent.fire(new GameEvent(this, pieceMap));
        return true;
    }

    @Override
    public boolean hasPiece(Location source) {
        return pieceMap.get(source) != null;
    }

    @Override @Nullable
    public AbstractPiece getPiece(Location source) {
        return pieceMap.get(source);
    }

    /**
     * Updates the piece map by moving the pieces
     * @param from The source tile
     * @param to The destination tile
     */
    private void updatePieces(Location from, Location to) {;
        AbstractPiece deferredPiece = this.pieceMap.get(from);
        this.pieceMap.put(from, null);
        this.pieceMap.put(to, deferredPiece);
    }

    // Events

    @Override
    public void onCapture(@NotNull CaptureEvent event) {
        if(!event.getBoardID().equals(this.id)) return;
        if(event.getPiece() instanceof King) {
            GameFinishEvent.fire(new GameFinishEvent(this, pieceMap, event.getCapturingSide()));
            finish();
        }
        this.pieceMap.put(event.getLocation(), null);
    }

    @Override // If a pawn makes a move, it'll call this method each time.
    public void onPromote(@NotNull PromotionEvent event) {
        if(!event.getBoardID().equals(this.id)) return;
        if(!(event.getPiece() instanceof Pawn pawn)) return;
        // Check for promotion rule for validity of promotion
        switch(promotionRule) {
            case DISABLED -> {
            }
            case HALFWAY -> {
                boolean canPromote = switch(pawn.getSide()) {
                    case WHITE -> pawn.getLocation().y() >= (y / 2 + 1);
                    case BLACK -> pawn.getLocation().y() <= (y / 2 - 1);
                };
                if(canPromote) {
                    pieceMap.put(event.getLocation(), event.getPromotion());
                }
            }
            case STANDARD -> {
                boolean canPromote = switch(pawn.getSide()) {
                    case WHITE -> pawn.getLocation().y() >= y;
                    case BLACK -> pawn.getLocation().y() <= 0;
                };
                if(canPromote) {
                    pieceMap.put(event.getLocation(), event.getPromotion());
                }
            }
        }
    }

    /* - - - Reserved mostly for Board class for board initializations - - - */

    GameBoard syncUUID() {
        if(!this.update) throw new IllegalStateException("Updates cannot be made anymore!");
        for(Map.Entry<Location, AbstractPiece> entry : this.pieceMap.entrySet()) {
            entry.getValue().setBoardID(this.id);
            entry.getValue().setGameBoard(this);
        }
        return this;
    }

    GameBoard setPromotionRule(PromotionRule rule) {
        if(!this.update) throw new IllegalStateException("Updates cannot be made anymore!");
        this.promotionRule = rule;
        return this;
    }

    GameBoard freezeUpdates() {
        this.update = false;
        return this;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override
    public void finish() {
        CaptureEvent.removeListener(this);
        PromotionEvent.removeListener(this);
        pieceMap.clear();
        activeGameBoards.remove(this);
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public boolean isOutside(Location location) {
        return location.x() < 0 || location.x() >= x || location.y() < 0 || location.y() >= y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Returns a String representation of how the current board looks like
     * @return A text view of this board
     */
    public String getBoardView() {
        StringBuilder ret = new StringBuilder("╔");
        for(int i = 0; i <= x; i++) {
            ret.append(i == x ? "═╗╮" : "═╤");
        }
        ret.append("\n");
        for(int axisY = y; axisY >= 0; axisY--) {
            ret.append("║");
            for(int axisX = 0; axisX < x; axisX++) {
                AbstractPiece piece = pieceMap.get(Location.of(axisX, axisY));
                String append;
                if(piece == null) {
                    append = "░";
                } else {
                    append = piece.character();
                }
                ret.append(append).append(axisX + 1 == x ? "" : "│");
            }
            ret.append("║").append(axisY + 1).append("\n");
        }
        ret.append("╚");
        for(int i = 0; i <= x; i++) {
            ret.append(x == i ? "═╝" : "═╧");
        }
        ret.append("\n");
        ret.append("╰");
        for(int i = 0; i < x; i++) {
            ret.append(Location.toReflectedAddress(i)).append("┈");
        }
        ret.append("╯");
        return ret.toString();
    }

    /**
     * @return An immutable, view-only map
     */
    public Map<Location, AbstractPiece> getPieceMap() {
        return Collections.unmodifiableMap(pieceMap);
    }

    /**
     * Returns a String representation of how the current board looks like
     * @param formatter The formatter to format this string
     * @return A text view of this board
     */
    public String getBoardView(Formatter formatter) {
        return formatter.format(this.getBoardView());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameBoard gameBoard = (GameBoard) o;
        return x == gameBoard.x && y == gameBoard.y && id.equals(gameBoard.id) && pieceMap.equals(gameBoard.pieceMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, id, pieceMap);
    }
}
