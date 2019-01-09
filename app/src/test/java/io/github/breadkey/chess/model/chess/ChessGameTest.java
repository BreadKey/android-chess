package io.github.breadkey.chess.model.chess;

import org.junit.Before;
import org.junit.Test;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.ChessRuleManager;
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
        printChessBoard();
    }

    @Test
    public void move_a2PawnTo_a4() {
        chessGame.move('a', 2, 'a', 4);
        assertEquals(ChessPiece.Type.Pawn, chessGame.getPieceAt('a', 4).type);
        assertNull(chessGame.getPieceAt('a', 2));
        assertEquals(ChessGame.Division.Black, chessGame.getCurrentTurn());
        printChessBoard();
    }

    @Test
    public void move_a2PawnTo_a5WhichCanNotMove() {
        chessGame.move('a',2, 'a', 5);
        assertNull(chessGame.getPieceAt('a', 5));
        assertEquals(ChessPiece.Type.Pawn, chessGame.getPieceAt('a', 2).type);
        assertEquals(ChessGame.Division.White, chessGame.getCurrentTurn());
    }

    @Test
    public void move_a5To_a6WhichNullPiece() {
        chessGame.move('a', 5, 'a', 6);
        assertNull(chessGame.getPieceAt('a', 5));
        assertNull(chessGame.getPieceAt('a', 6));
        assertEquals(ChessGame.Division.White, chessGame.getCurrentTurn());
    }

    @Test
    public void pawnCanNotMove2SquareAfterMove() {
        chessGame.move('a', 2, 'a', 3);
        chessGame.move('a', 7, 'a', 6);
        chessGame.move('a', 3, 'a', 5);

        assertNull(chessGame.getPieceAt('a', 5));
        assertEquals(ChessPiece.Type.Pawn, chessGame.getPieceAt('a', 3).type);
        assertEquals(ChessGame.Division.White, chessGame.getCurrentTurn());
        printChessBoard();
    }

    @Test
    public void kill() {
        chessGame.move('a', 2, 'a', 4);
        chessGame.move('b', 7, 'b', 5);
        chessGame.move('a', 4, 'b', 5);

        assertEquals(ChessGame.Division.White, chessGame.getPieceAt('b', 5).division);
        assertEquals(1, chessGame.getPieceAt('b', 5).killScore);
        printChessBoard();
    }

    @Test
    public void check() {
        chessGame.chessBoard = new ChessBoard();
        chessGame.chessBoard.placePiece('a', 1, new Rook(ChessGame.Division.White));
        chessGame.chessBoard.placePiece('b', 8,chessGame.kingHashMap.get(ChessGame.Division.Black));

        chessGame.move('a', 1, 'b' ,1);
        assertTrue(chessGame.kingHashMap.get(ChessGame.Division.Black).isChecked());
    }

    private void printChessBoard() {
        System.out.println(chessGame.chessBoard);
        System.out.println("Current Turn: " + chessGame.getCurrentTurn());
    }
}