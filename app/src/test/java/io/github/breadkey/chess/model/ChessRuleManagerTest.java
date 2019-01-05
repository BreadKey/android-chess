package io.github.breadkey.chess.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.github.breadkey.chess.model.chessPieces.Pawn;

import static org.junit.Assert.*;

public class ChessRuleManagerTest {
    ChessRuleManager ruleManager = ChessRuleManager.getInstance();
    ChessBoard chessBoard;

    @Before
    public void setUp() {
        chessBoard = new ChessBoard();
    }

    @Test
    public void find_a2PawnCanMoveCoordinates() {
        chessBoard.placePiece('a', 2, new Pawn(ChessGame.Division.White));

        List<Coordinate> canMoveCoordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'a', 2);;

        assertCoordinatesContains('a', 3, canMoveCoordinates);
        assertCoordinatesContains('a', 4, canMoveCoordinates);
    }

    @Test
    public void find_a7PawnCanMoveCoordinates() {
        chessBoard.placePiece('a', 7, new Pawn(ChessGame.Division.Black));

        List<Coordinate> canMoveCoordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'a', 7);

        assertCoordinatesContains('a', 6, canMoveCoordinates);
        assertCoordinatesContains('a', 5, canMoveCoordinates);
    }

    @Test
    public void find_a2PawnCanMoveCoordinatesWhenAnotherPieceIsExistInFront() {
        chessBoard.placePiece('a', 2, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('a', 3, new Pawn(ChessGame.Division.Black));

        List<Coordinate> canMoveCoordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'a', 2);

        assertEquals(0, canMoveCoordinates.size());
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
}