package com.spiritlight.chess.game;

import com.spiritlight.chess.pieces.*;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.PromotionRule;
import com.spiritlight.chess.utils.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Board {
    private static final Map<Location, AbstractPiece> DEFAULT = new HashMap<>() {{
        put(Location.of('a', 7), new Rook(null, Location.of('a', 7), Side.BLACK));
        put(Location.of('b', 7), new Knight(null, Location.of('b', 7), Side.BLACK));
        put(Location.of('c', 7), new Bishop(null, Location.of('c', 7), Side.BLACK));
        put(Location.of('d', 7), new Queen(null, Location.of('d', 7), Side.BLACK));
        put(Location.of('e', 7), new King(null, Location.of('e', 7), Side.BLACK));
        put(Location.of('f', 7), new Bishop(null, Location.of('f', 7), Side.BLACK));
        put(Location.of('g', 7), new Knight(null, Location.of('g', 7), Side.BLACK));
        put(Location.of('h', 7), new Rook(null, Location.of('h', 7), Side.BLACK));

        put(Location.of('a', 0), new Rook(null, Location.of('a', 0), Side.WHITE));
        put(Location.of('b', 0), new Knight(null, Location.of('b', 0), Side.WHITE));
        put(Location.of('c', 0), new Bishop(null, Location.of('c', 0), Side.WHITE));
        put(Location.of('d', 0), new Queen(null, Location.of('d', 0), Side.WHITE));
        put(Location.of('e', 0), new King(null, Location.of('e', 0), Side.WHITE));
        put(Location.of('f', 0), new Bishop(null, Location.of('f', 0), Side.WHITE));
        put(Location.of('g', 0), new Knight(null, Location.of('g', 0), Side.WHITE));
        put(Location.of('h', 0), new Rook(null, Location.of('h', 0), Side.WHITE));
        for(int i = 0; i < 8; i++) {
            put(Location.of(i, 6), new Pawn(null, Location.of(i, 6), Side.BLACK));
            put(Location.of(i, 1), new Pawn(null, Location.of(i, 1), Side.WHITE));
        }
    }};

    /**
     * Initializes a new {@link GameBoard} instance for the game containing
     * the classic layout of chess and syncs the pieces' UUID
     * to the board.
     * @return A fully initialized {@link GameBoard} with the classic layout
     */
    public static GameBoard newClassicBoard() {
        return new GameBoard(7, 7, UUID.randomUUID(), new HashMap<>(DEFAULT), PromotionRule.STANDARD).syncUUID().freezeUpdates();
    }

    public static Map<Location, AbstractPiece> getDefaultLayout() {
        return new HashMap<>(DEFAULT);
    }

    public static class Builder {
        private int width;
        private int height;
        private Map<Location, AbstractPiece> pieceMap = new HashMap<>();
        private PromotionRule rule;
        private boolean syncUUID = false;

        /**
         * Constructs a new builder
         * @param width The width of the board
         * @param height The height of the board
         */
        public static Builder of(int width, int height) {
            return new Builder()
                    .setWidth(width)
                    .setHeight(height);
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder addPiece(char x, char y, AbstractPiece piece) {
            return this.addPiece(Location.of(x, y), piece);
        }

        public Builder addPiece(char x, int y, AbstractPiece piece) {
            return this.addPiece(Location.of(x, y), piece);
        }

        public Builder addPiece(int x, int y, AbstractPiece piece) {
            return this.addPiece(Location.of(x, y), piece);
        }

        public Builder addPiece(Location location, AbstractPiece piece) {
            this.pieceMap.put(location, piece);
            return this;
        }

        public Builder setPieceMap(Map<Location, AbstractPiece> map) {
            this.pieceMap = map;
            return this;
        }

        public Builder syncUUID() {
            this.syncUUID = true;
            return this;
        }

        public Builder setPromotionRule(PromotionRule rule) {
            this.rule = rule;
            return this;
        }

        public GameBoard build() {
            if(width < 0 || height < 0) throw new IllegalArgumentException("Width and height cannot be less than 0");
            if(pieceMap == null) {
                pieceMap = new HashMap<>();
            }
            if(rule == null) {
                rule = PromotionRule.STANDARD;
            }
            GameBoard board = new GameBoard(width, height, UUID.randomUUID(), pieceMap, rule);
            if(syncUUID) {
                board.syncUUID();
            }
            return board;
        }
    }
}
