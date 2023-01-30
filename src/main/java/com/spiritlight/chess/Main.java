package com.spiritlight.chess;

import com.spiritlight.chess.events.GameFinishEvent;
import com.spiritlight.chess.game.Board;
import com.spiritlight.chess.game.GameBoard;
import com.spiritlight.chess.utils.Location;
import com.spiritlight.chess.utils.Side;

import java.util.Scanner;

// fix: Knight movement issues
// fix: Pawn capturing
public class Main implements GameFinishEvent.Listener {
    public static void main(String[] args) {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        GameBoard board = Board.newClassicBoard();
        Side play = Side.WHITE;
        final Scanner scanner = new Scanner(System.in);
        while(!board.isCompleted()) {
            System.out.println(board.getBoardView());
            System.out.println("Current in move: " + play);
            System.out.println("Move command example: a2,a4 = a2 to a4");
            boolean moveLegal = false;
            while(!moveLegal) {
                String[] move = scanner.nextLine().split(",");
                Location from = Location.of(move[0].charAt(0), Integer.parseInt(move[0].substring(1, 2)) - 1);
                System.out.println(from);
                Location to = Location.of(move[1].charAt(0), Integer.parseInt(move[1].substring(1, 2)) - 1);
                System.out.println(to);
                moveLegal = board.movePiece(play, from, to);
                if(!moveLegal) {
                    System.out.println("Please make a valid move! Try again...");
                } else {
                    play = play == Side.WHITE ? Side.BLACK : Side.WHITE;
                }
            }
        }
    }

    @Override
    public void onFinish(GameFinishEvent e) {
        System.out.println(e.getWinner() + " won!");
    }
}