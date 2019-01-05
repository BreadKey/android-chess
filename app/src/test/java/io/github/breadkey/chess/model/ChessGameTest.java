package io.github.breadkey.chess.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ChessGameTest {
    ChessGame chessGame;
    ChessRuleManager ruleManager = ChessRuleManager.getInstance();

    @Before
    public void setUp() {
        chessGame = new ChessGame();
    }

    @Test
    public void boardIsInitializedCorrectly() {
        printChessBoard();
    }

    @Test
    public void find_a2PawnCanMoveCoordinates() {
        List<Coordinate> canMoveCoordinates = ruleManager.findSquareCoordinateCanMove(chessGame.chessBoard, 'a', 2);;

        assertCoordinatesContains('a', 3, canMoveCoordinates);
        assertCoordinatesContains('a', 4, canMoveCoordinates);
    }

    @Test
    public void find_a7PawnCanMoveCoordinates() {
        List<Coordinate> canMoveCoordinates = ruleManager.findSquareCoordinateCanMove(chessGame.chessBoard, 'a', 7);

        assertCoordinatesContains('a', 6, canMoveCoordinates);
        assertCoordinatesContains('a', 5, canMoveCoordinates);
    }

    private void assertCoordinatesContains(char file, int rank, List<Coordinate> coordinates) {
        boolean isContain = false;

        for (Coordinate coordinate: coordinates) {
            if (coordinate.getFile() == file && coordinate.getRank() == rank) {
                isContain = true;
                break;
            }
        }

        assertTrue(isContain);
    }

    private void printChessBoard() {
        for(int rank: ChessBoard.ranks) {
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