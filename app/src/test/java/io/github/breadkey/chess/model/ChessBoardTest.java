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
    public void squaresAreCreated() {
        Square firstSquare = chessBoard.squares[0][0];

        assertNotEquals(null, firstSquare);
    }

    @Test
    public void placePawnAt_e1() {
        chessBoard.placePiece('e', 1, new Pawn(ChessGame.Division.Black));

        ChessPiece.Type actualPieceOn_e1Type = chessBoard.getPieceAt('e', 1).getType();
        assertEquals(ChessPiece.Type.Pawn, actualPieceOn_e1Type);
    }

    @Test
    public void placePawnOutOfRankRange() {
        chessBoard.placePiece('a', -4, new Pawn(ChessGame.Division.Black));
        chessBoard.placePiece('a', 9, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('q', 1, new Pawn(ChessGame.Division.White));
        chessBoard.placePiece('z', 1, new Pawn(ChessGame.Division.Black));

        assertEquals(0, chessBoard.countPieces());
    }
}