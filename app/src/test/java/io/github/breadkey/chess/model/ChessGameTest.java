package io.github.breadkey.chess.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChessGameTest {
    ChessGame chessGame;
    @Before
    public void setUp() {
        chessGame = new ChessGame();
    }

    @Test
    public void boardIsInitializedCorrectly() {
        printChessBoard();
    }

    private void printChessBoard() {
        List<Integer> reversedRanks = new ArrayList<>();
        for(int rank: ChessBoard.ranks) {
            reversedRanks.add(rank);
        }
        Collections.reverse(reversedRanks);

        for(int rank: reversedRanks) {
            System.out.print(String.valueOf(rank) + "\t");
            for(char file: ChessBoard.files) {
                ChessPiece piece = chessGame.getPieceAt(file, rank);
                if (piece != null) {
                    System.out.print(piece.type.toString() + "\t");
                }
            }
            System.out.println();
        }
        for(char file: ChessBoard.files) {
            System.out.print("\t" + file + "\t");
        }
    }
}