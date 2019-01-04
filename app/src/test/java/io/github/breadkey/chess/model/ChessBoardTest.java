package io.github.breadkey.chess.model;

import org.junit.Before;
import org.junit.Test;

import io.github.breadkey.chess.model.chessPieces.Pawn;

import static org.junit.Assert.*;

public class ChessBoardTest {
   ChessBoard chessBoard;

    @Before
   public void setUp() {
       chessBoard = new ChessBoard();
   }

    @Test
    public void fileValueIsCorrect() {
        int aValue = ChessBoard.Files.a.getValue();

        assertEquals(0, aValue);
    }

    @Test
    public void squaresAreCreated() {
        Square firstSquare = chessBoard.squares[0][0];

        assertNotEquals(null, firstSquare);
    }

    @Test
    public void placePawnAt_e1() {
        chessBoard.placePiece(ChessBoard.Files.e, 1, new Pawn());

        ChessPiece.Type actualPieceOn_e1Type = chessBoard.getPieceAt(ChessBoard.Files.e, 1).getType();
        assertEquals(ChessPiece.Type.Pawn, actualPieceOn_e1Type);
    }

    @Test
    public void placePawnOutOfRankRange() {
        chessBoard.placePiece(ChessBoard.Files.a, -4, new Pawn());
        chessBoard.placePiece(ChessBoard.Files.a, 8, new Pawn());

        assertEquals(0, chessBoard.countPieces());
    }
}