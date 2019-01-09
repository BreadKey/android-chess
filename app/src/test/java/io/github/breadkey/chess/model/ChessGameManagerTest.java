package io.github.breadkey.chess.model;

import org.junit.Before;
import org.junit.Test;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ChessGameManagerTest {
    ChessGameManager chessGameManager = new TestChessGameManager();
    Player whitePlayer;

    @Before
    public void setUp() {
        chessGameManager.startNewChess(new Player("영기"), new Player("현정"));
    }

    @Test
    public void move_a2PawnTo_a4() {
        chessGameManager.select('a', 2);
        chessGameManager.select('a', 4);

        assertNull(chessGameManager.getChessGame().getPieceAt('a', 2));
    }

    @Test
    public void tryMoveBlackPieceAtFirst() {
        chessGameManager.select('b', 7);
        chessGameManager.select('b', 5);

        assertNull(chessGameManager.getChessGame().getPieceAt('b', 5));
    }

    @Test
    public void kill_b7PawnWith_a2Pawn() {
        chessGameManager.select('a', 2);
        chessGameManager.select('a', 4);
        chessGameManager.select('b', 7);
        chessGameManager.select('b', 5);
        chessGameManager.select('a', 4);
        chessGameManager.select('b', 5);

        assertEquals(ChessGame.Division.White, chessGameManager.getChessGame().getPieceAt('b', 5).division);
        assertEquals(1, chessGameManager.getChessGame().getPieceAt('b', 5).killScore);
    }

    @Test
    public void move_a2PawnTo_b3WhereCanNotGo() {
        chessGameManager.select('a', 2);
        chessGameManager.select('b',3);

        assertNull(chessGameManager.getChessGame().getPieceAt('b', 3));
        assertEquals(0, chessGameManager.getMoves().size());
    }

    @Test
    public void undoMove() {
        chessGameManager.select('a', 2);
        chessGameManager.select('a', 4);
        chessGameManager.select('b', 7);
        chessGameManager.select('b', 5);

        chessGameManager.undoMove(chessGameManager.getMoves().get(chessGameManager.getMoves().size() - 1));
        assertEquals(1, chessGameManager.getMoves().size());
        assertNull(chessGameManager.getChessGame().getPieceAt('b', 5));
        assertEquals(ChessPiece.Type.Pawn, chessGameManager.getChessGame().getPieceAt('b', 7).type);
    }

    @Test
    public void undoMoveAfterKill() {
        chessGameManager.select('a', 2);
        chessGameManager.select('a', 4);
        chessGameManager.select('b', 7);
        chessGameManager.select('b', 5);
        chessGameManager.select('a', 4);
        chessGameManager.select('b', 5);

        chessGameManager.undoMove(chessGameManager.getMoves().get(chessGameManager.getMoves().size() - 1));
        assertEquals(ChessPiece.Type.Pawn, chessGameManager.getChessGame().getPieceAt('a', 4).type);
        assertEquals(ChessPiece.Type.Pawn, chessGameManager.getChessGame().getPieceAt('b', 5).type);
        assertEquals(ChessGame.Division.Black, chessGameManager.getChessGame().getPieceAt('b', 5).division);
        assertEquals(0, chessGameManager.getChessGame().getPieceAt('a' ,4).killScore);
        assertEquals(16, chessGameManager.getChessGame().getPieces(ChessGame.Division.Black).size());
    }

    @Test
    public void undoLastMoveOfWhite() {
        chessGameManager.select('a', 2);
        chessGameManager.select('a', 4);
        chessGameManager.select('b', 7);
        chessGameManager.select('b', 5);

        chessGameManager.undoMoves(ChessGame.Division.White);
        assertEquals(0, chessGameManager.getMoves().size());
        assertNull(chessGameManager.getChessGame().getPieceAt('b', 5));
        assertNull(chessGameManager.getChessGame().getPieceAt('a', 4));
        assertEquals(ChessPiece.Type.Pawn, chessGameManager.getChessGame().getPieceAt('b', 7).type);
        assertEquals(ChessPiece.Type.Pawn, chessGameManager.getChessGame().getPieceAt('a', 2).type);
        assertEquals(ChessGame.Division.White, chessGameManager.getChessGame().getCurrentTurn());
    }

    @Test
    public void undoLastMovesOfBlackAfterKill() {
        chessGameManager.select('a', 2);
        chessGameManager.select('a', 4);
        chessGameManager.select('b', 7);
        chessGameManager.select('b', 5);
        chessGameManager.select('a', 4);
        chessGameManager.select('b', 5);

        chessGameManager.undoMoves(ChessGame.Division.Black);
        assertEquals(1, chessGameManager.getMoves().size());
        assertEquals(ChessPiece.Type.Pawn, chessGameManager.getChessGame().getPieceAt('a', 4).type);
        assertEquals(0, chessGameManager.getChessGame().getPieceAt('a', 4).killScore);
        assertEquals(ChessPiece.Type.Pawn, chessGameManager.getChessGame().getPieceAt('b', 7).type);
        assertEquals(ChessGame.Division.Black, chessGameManager.getChessGame().getCurrentTurn());
    }


    @Test
    public void undoBlackLastMovesWhenWhiteTurn() {
        chessGameManager.select('a', 2);
        chessGameManager.select('a', 4);
        chessGameManager.select('b', 7);
        chessGameManager.select('b', 5);

        chessGameManager.undoMoves(ChessGame.Division.Black);
        assertEquals(ChessGame.Division.Black, chessGameManager.getChessGame().getCurrentTurn());
    }
}