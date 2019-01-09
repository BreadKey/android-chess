package io.github.breadkey.chess.model.chess;

import org.junit.Before;
import org.junit.Test;

import io.github.breadkey.chess.model.chess.chessPieces.King;
import io.github.breadkey.chess.model.chess.chessPieces.Rook;

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
        System.out.println(chessGame);
    }

    @Test
    public void move_a2PawnTo_a4() {
        chessGame.tryMove('a', 2, 'a', 4);
        assertEquals(ChessPiece.Type.Pawn, chessGame.getPieceAt('a', 4).type);
        assertNull(chessGame.getPieceAt('a', 2));
        assertEquals(ChessGame.Division.Black, chessGame.getCurrentTurn());
        System.out.println(chessGame);
    }

    @Test
    public void move_a2PawnTo_a5WhichCanNotMove() {
        chessGame.tryMove('a',2, 'a', 5);
        assertNull(chessGame.getPieceAt('a', 5));
        assertEquals(ChessPiece.Type.Pawn, chessGame.getPieceAt('a', 2).type);
        assertEquals(ChessGame.Division.White, chessGame.getCurrentTurn());
    }

    @Test
    public void move_a5To_a6WhichNullPiece() {
        chessGame.tryMove('a', 5, 'a', 6);
        assertNull(chessGame.getPieceAt('a', 5));
        assertNull(chessGame.getPieceAt('a', 6));
        assertEquals(ChessGame.Division.White, chessGame.getCurrentTurn());
    }

    @Test
    public void pawnCanNotMove2SquareAfterMove() {
        chessGame.tryMove('a', 2, 'a', 3);
        chessGame.tryMove('a', 7, 'a', 6);
        chessGame.tryMove('a', 3, 'a', 5);

        assertNull(chessGame.getPieceAt('a', 5));
        assertEquals(ChessPiece.Type.Pawn, chessGame.getPieceAt('a', 3).type);
        assertEquals(ChessGame.Division.White, chessGame.getCurrentTurn());
        System.out.println(chessGame);
    }

    @Test
    public void kill() {
        chessGame.tryMove('a', 2, 'a', 4);
        chessGame.tryMove('b', 7, 'b', 5);
        chessGame.tryMove('a', 4, 'b', 5);

        assertEquals(ChessGame.Division.White, chessGame.getPieceAt('b', 5).division);
        assertEquals(1, chessGame.getPieceAt('b', 5).killScore);
        System.out.println(chessGame);
    }

    @Test
    public void check() {
        chessGame.clearChessBoard();
        chessGame.placeNewPiece('a', 1, new Rook(ChessGame.Division.White));
        chessGame.placeNewPiece('b', 8, new King(ChessGame.Division.Black));

        chessGame.tryMove('a', 1, 'b' ,1);
        assertTrue(chessGame.kingHashMap.get(ChessGame.Division.Black).isChecked());
    }

    @Test
    public void isBlackPiecesCanMove_d5() {
        assertTrue(chessGame.isPiecesCanMove(ChessGame.Division.Black, 'd', 5));
    }

    @Test
    public void isWhitePiecesCanMove_b7WhenRookOn_a1() {
        chessGame.clearChessBoard();
        chessGame.placeNewPiece('a' ,1, new Rook(ChessGame.Division.White));
        assertFalse(chessGame.isPiecesCanMove(ChessGame.Division.White, 'b', 7));
    }

    @Test
    public void tryMoveKingWhereCanDeadAfterCheck() {
        chessGame.clearChessBoard();
        chessGame.placeNewPiece('a', 1, new Rook(ChessGame.Division.White));
        chessGame.placeNewPiece('b', 8, new King(ChessGame.Division.Black));

        chessGame.tryMove('a', 1, 'b' ,1);
        chessGame.tryMove('b', 8, 'b', 7);
        assertNull(chessGame.getPieceAt('b', 7));
        assertEquals(ChessGame.Division.Black, chessGame.getCurrentTurn());
    }

    @Test
    public void tryMoveRookWhereCanMakeKingDead() {
        chessGame.clearChessBoard();
        chessGame.placeNewPiece('a', 1, new King(ChessGame.Division.White));
        chessGame.placeNewPiece('a', 2, new Rook(ChessGame.Division.White));
        chessGame.placeNewPiece('a', 8, new Rook(ChessGame.Division.Black));

        chessGame.tryMove('a', 2, 'b', 2);
        assertNull(chessGame.getPieceAt('b' ,2));
        assertEquals(ChessGame.Division.White, chessGame.getCurrentTurn());
    }
}