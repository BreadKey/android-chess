package io.github.breadkey.chess.model.chess;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.github.breadkey.chess.model.chess.ChessBoard;
import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessRuleManager;
import io.github.breadkey.chess.model.chess.Coordinate;
import io.github.breadkey.chess.model.chess.chessPieces.Bishop;
import io.github.breadkey.chess.model.chess.chessPieces.King;
import io.github.breadkey.chess.model.chess.chessPieces.Knight;
import io.github.breadkey.chess.model.chess.chessPieces.Pawn;
import io.github.breadkey.chess.model.chess.chessPieces.Queen;
import io.github.breadkey.chess.model.chess.chessPieces.Rook;

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
        System.out.println(canMoveCoordinates);
        assertEquals(0, canMoveCoordinates.size());
    }

    @Test
    public void findWhitePawnCoordinateWhereCanKillEnemyPiece() {
        chessBoard.placePiece('a', 2, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('b', 3, new Pawn(ChessGame.Division.Black));

        List<Coordinate> canMoveCoordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'a', 2);
        assertCoordinatesContains('b', 3, canMoveCoordinates);
    }

    @Test
    public void find_d4KnightCanMoveCoordinates() {
        chessBoard.placePiece('d', 4, new Knight(ChessGame.Division.White));

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'd', 4);
        assertCoordinatesContains('c', 6, coordinates);
        assertCoordinatesContains('e', 6, coordinates);
        assertCoordinatesContains('c', 2, coordinates);
        assertCoordinatesContains('e', 2, coordinates);
        assertCoordinatesContains('b', 5, coordinates);
        assertCoordinatesContains('b', 3, coordinates);
        assertCoordinatesContains('f', 5, coordinates);
        assertCoordinatesContains('f', 3, coordinates);
    }

    @Test
    public void find_b1KnightCanMoveCoordinatesWhenBoardInitialized() {
        ChessGame chessGame = new ChessGame();
        chessBoard = chessGame.chessBoard;

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'b', 1);
        assertEquals(2, coordinates.size());
        assertCoordinatesContains('a', 3, coordinates);
        assertCoordinatesContains('c', 3, coordinates);
    }

    @Test
    public void canNotMove_b1KnightTo_a3BecauseAlly() {
        chessBoard.placePiece('b', 1, new Knight(ChessGame.Division.White));
        chessBoard.placePiece('a', 3, new Pawn(ChessGame.Division.White));

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard,'b', 1);
        assertEquals(2, coordinates.size());
        assertCoordinatesNotContains('a', 3, coordinates);
    }

    @Test
    public void canMove_b1KnightTo_a3WhereEnemyPlaced() {
        chessBoard.placePiece('b', 1, new Knight(ChessGame.Division.White));
        chessBoard.placePiece('a', 3, new Pawn(ChessGame.Division.Black));

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'b', 1);
        assertEquals(3, coordinates.size());
        assertCoordinatesContains('a', 3, coordinates);
    }

    @Test
    public void find_d4RookCanMoveCoordinates() {
        chessBoard.placePiece('d', 4, new Rook(ChessGame.Division.White));

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'd', 4);
        for (char file: ChessBoard.files) {
            if (file == 'd') { continue; }
            assertCoordinatesContains(file, 4, coordinates);
        }
        for (int rank: ChessBoard.ranks) {
            if (rank == 4) { continue; }
            assertCoordinatesContains('d', rank, coordinates);
        }
        assertCoordinatesNotContains('d', 4, coordinates);
    }

    @Test
    public void find_d4RookCanMoveCoordinatesWhenIsBlockedEveryDirections() {
        chessBoard.placePiece('d', 4, new Rook(ChessGame.Division.White));
        chessBoard.placePiece('d', 3, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('d', 5, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('c', 4, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('e', 4, new Pawn(ChessGame.Division.White));

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'd', 4);
        assertEquals(0, coordinates.size());
    }

    @Test
    public void find_d4RookCanMoveCoordinatesWhenIsBlockedEveryDirectionsByEnemy() {
        chessBoard.placePiece('d', 4, new Rook(ChessGame.Division.White));
        chessBoard.placePiece('d', 3, new Pawn(ChessGame.Division.Black));
        chessBoard.placePiece('d', 5, new Pawn(ChessGame.Division.Black));
        chessBoard.placePiece('c', 4, new Pawn(ChessGame.Division.Black));
        chessBoard.placePiece('e', 4, new Pawn(ChessGame.Division.Black));

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'd', 4);
        assertEquals(4, coordinates.size());
        assertCoordinatesContains('d', 3, coordinates);
        assertCoordinatesContains('d', 5, coordinates);
        assertCoordinatesContains('c', 4, coordinates);
        assertCoordinatesContains('e', 4, coordinates);
    }

    @Test
    public void find_d4BishopCanMoveCoordinates() {
        chessBoard.placePiece('d', 4, new Bishop(ChessGame.Division.White));
        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'd', 4);

        char[] rightDownDiagonalFileRange = {'a', 'b', 'c', 'e', 'f', 'g'};
        int[] rightDownDiagonalRankRange = {7, 6, 5, 3, 2, 1};
        for (int i = 0; i < rightDownDiagonalFileRange.length; i++) {
            assertCoordinatesContains(rightDownDiagonalFileRange[i], rightDownDiagonalRankRange[i], coordinates);
        }
        char[] rightUpDiagonalFileRange = {'a', 'b', 'c', 'e', 'f', 'g'};
        int[] rightUpDiagonalRankRange = {1, 2, 3, 5, 6, 7};
        for (int i = 0; i < rightUpDiagonalFileRange.length; i++) {
            assertCoordinatesContains(rightUpDiagonalFileRange[i], rightUpDiagonalRankRange[i], coordinates);
        }
    }

    @Test
    public void find_d4BishopCanMoveCoordinatesWhenBlockedEveryDirections() {
        chessBoard.placePiece('d', 4, new Bishop(ChessGame.Division.White));
        chessBoard.placePiece('c', 5, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('e', 5, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('c', 3, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('e', 3, new Pawn(ChessGame.Division.White));

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'd', 4);
        assertEquals(0, coordinates.size());
    }

    @Test
    public void find_d4BishopCanMoveCoordinatesWhenBlockedEveryDirectionsByEnemy() {
        chessBoard.placePiece('d', 4, new Bishop(ChessGame.Division.White));
        chessBoard.placePiece('c', 5, new Pawn(ChessGame.Division.Black));
        chessBoard.placePiece('e', 5, new Pawn(ChessGame.Division.Black));
        chessBoard.placePiece('c', 3, new Pawn(ChessGame.Division.Black));
        chessBoard.placePiece('e', 3, new Pawn(ChessGame.Division.Black));

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'd', 4);
        assertEquals(4, coordinates.size());
        assertCoordinatesContains('c', 5, coordinates);
        assertCoordinatesContains('e', 5, coordinates);
        assertCoordinatesContains('c', 3, coordinates);
        assertCoordinatesContains('e', 3, coordinates);
    }

    @Test
    public void find_a1QueenCanMoveCoordinates() {
        chessBoard.placePiece('a', 1, new Queen(ChessGame.Division.White));

        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'a', 1);

        assertEquals(21, coordinates.size());

        char[] rightFiles = {'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int[] upRanks = {2, 3, 4, 5, 6, 7, 8};

        for (char rightFile: rightFiles) {
            assertCoordinatesContains(rightFile, 1, coordinates);
        }
        for (int upRank: upRanks) {
            assertCoordinatesContains('a', upRank, coordinates);
        }
        for (int diagonalIndex = 1; diagonalIndex < upRanks.length; diagonalIndex++) {
            assertCoordinatesContains(rightFiles[diagonalIndex], upRanks[diagonalIndex], coordinates);
        }
    }

    @Test
    public void find_d4KingCanMoveCoordinates() {
        chessBoard.placePiece('d', 4, new King(ChessGame.Division.White));
        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'd', 4);

        assertEquals(8, coordinates.size());
        assertCoordinatesContains('c', 4, coordinates);
        assertCoordinatesContains('c', 5, coordinates);
        assertCoordinatesContains('d', 5, coordinates);
        assertCoordinatesContains('e', 5, coordinates);
        assertCoordinatesContains('e', 4, coordinates);
        assertCoordinatesContains('e', 3, coordinates);
        assertCoordinatesContains('d', 3, coordinates);
        assertCoordinatesContains('c', 3, coordinates);
    }

    @Test
    public void find_e1KingCanMoveCoordinatesWhenBoardInitialized() {
        chessBoard = new ChessGame().chessBoard;
        List<Coordinate> coordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, 'e', 1);
        assertEquals(0, coordinates.size());
    }

    private void assertCoordinatesContains(char file, int rank, List<Coordinate> coordinates) {
        assertTrue(isContain(file, rank, coordinates));
    }

    private void assertCoordinatesNotContains(char file, int rank, List<Coordinate> coordinates) {
        assertTrue(!isContain(file, rank, coordinates));
    }

    private boolean isContain(char file, int rank, List<Coordinate> coordinates) {
        boolean isContain = false;

        for (Coordinate coordinate: coordinates) {
            if (coordinate.getFile() == file && coordinate.getRank() == rank) {
                isContain = true;
                break;
            }
        }

        return isContain;
    }
}