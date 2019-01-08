package io.github.breadkey.chess.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
}