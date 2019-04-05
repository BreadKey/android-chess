package io.github.breadkey.chess.model.chess;

import org.junit.Before;
import org.junit.Test;

import io.github.breadkey.chess.mock.MockPlayerHJ;
import io.github.breadkey.chess.mock.MockPlayerYK;
import io.github.breadkey.chess.model.chess.chessPieces.Bishop;
import io.github.breadkey.chess.model.chess.chessPieces.King;
import io.github.breadkey.chess.model.chess.chessPieces.Knight;
import io.github.breadkey.chess.model.chess.chessPieces.Queen;
import io.github.breadkey.chess.model.chess.chessPieces.Rook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PlayChessServiceTest {
    PlayChessService playChessService;

    @Before
    public void setUp() {
        playChessService = new PlayChessService();
        playChessService.startNewGame(new MockPlayerYK(), new MockPlayerHJ());
    }

    @Test
    public void boardIsInitializedCorrectly() {
        System.out.println(playChessService);
    }

    @Test
    public void move_a2PawnTo_a4() {
        playChessService.tryMove('a', 2, 'a', 4);
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('a', 4).type);
        assertNull(playChessService.getPieceAt('a', 2));
        assertEquals(PlayChessService.Division.Black, playChessService.getCurrentTurn());
        System.out.println(playChessService);
    }

    @Test
    public void move_a2PawnTo_a5WhichCanNotMove() {
        playChessService.tryMove('a',2, 'a', 5);
        assertNull(playChessService.getPieceAt('a', 5));
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('a', 2).type);
        assertEquals(PlayChessService.Division.White, playChessService.getCurrentTurn());
    }

    @Test
    public void move_a5To_a6WhichNullPiece() {
        playChessService.tryMove('a', 5, 'a', 6);
        assertNull(playChessService.getPieceAt('a', 5));
        assertNull(playChessService.getPieceAt('a', 6));
        assertEquals(PlayChessService.Division.White, playChessService.getCurrentTurn());
    }

    @Test
    public void pawnCanNotMove2SquareAfterMove() {
        playChessService.tryMove('a', 2, 'a', 3);
        playChessService.tryMove('a', 7, 'a', 6);
        playChessService.tryMove('a', 3, 'a', 5);

        assertNull(playChessService.getPieceAt('a', 5));
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('a', 3).type);
        assertEquals(PlayChessService.Division.White, playChessService.getCurrentTurn());
        System.out.println(playChessService);
    }

    @Test
    public void kill() {
        kill_b7PawnWith_a2Pawn();

        assertEquals(PlayChessService.Division.White, playChessService.getPieceAt('b', 5).division);
        assertEquals(1, playChessService.getPieceAt('b', 5).killScore);
        System.out.println(playChessService);
    }

    @Test
    public void tryMoveKingWhereCanDeadAfterCheck() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('a', 5, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('a', 1, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('b', 8, new King(PlayChessService.Division.Black));

        playChessService.tryMove('a', 1, 'b' ,1);
        playChessService.tryMove('b', 8, 'b', 7);
        assertNull(playChessService.getPieceAt('b', 7));
        assertEquals(PlayChessService.Division.Black, playChessService.getCurrentTurn());
    }

    @Test
    public void tryMoveRookWhereCanMakeKingDead() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('a', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('a', 2, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('a', 8, new Rook(PlayChessService.Division.Black));

        playChessService.tryMove('a', 2, 'b', 2);
        assertNull(playChessService.getPieceAt('b' ,2));
        assertEquals(PlayChessService.Division.White, playChessService.getCurrentTurn());
    }

    @Test
    public void isStatusUnchangedAfterFindCoordinatesWhereCanMakeKingDead() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('a', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('a', 2, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('a', 8, new Rook(PlayChessService.Division.Black));
        playChessService.placeNewPiece('b', 2, new Bishop(PlayChessService.Division.Black));

        playChessService.tryMove('a', 2, 'b', 2);
        assertEquals(ChessPiece.Type.Bishop, playChessService.getPieceAt('b', 2).type);
        assertEquals(ChessPiece.Type.Rook, playChessService.getPieceAt('a', 2).type);
    }

    @Test
    public void unCheckByMovingKingAfterCheck() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('a', 5, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('a', 1, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('b', 8, new King(PlayChessService.Division.Black));

        playChessService.tryMove('a', 1, 'b', 1);
        playChessService.tryMove('b', 8, 'a', 8);
        assertFalse(playChessService.getKing(PlayChessService.Division.Black).isChecked());
    }

    @Test
    public void unCheckByMovingAnotherPieceAfterCheck() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('d', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('h', 1, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('e', 8, new King(PlayChessService.Division.Black));
        playChessService.placeNewPiece('f', 8, new Bishop(PlayChessService.Division.Black));

        playChessService.tryMove('h', 1, 'e', 1);
        playChessService.tryMove('f', 8, 'e', 7);
        assertFalse(playChessService.getKing(PlayChessService.Division.Black).isChecked());
        assertEquals(ChessPiece.Type.Bishop, playChessService.getPieceAt('e', 7).type);
    }

    @Test
    public void unCheckByKillEnemyPiece() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('e', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('f', 5, new Knight(PlayChessService.Division.White));
        playChessService.placeNewPiece('e', 8, new King(PlayChessService.Division.Black));
        playChessService.placeNewPiece('f', 8, new Bishop(PlayChessService.Division.Black));

        playChessService.tryMove('f', 5, 'g', 7);
        playChessService.tryMove('f', 8, 'g', 7);
        assertFalse(playChessService.getKing(PlayChessService.Division.Black).isChecked());
        assertEquals(ChessPiece.Type.Bishop, playChessService.getPieceAt('g', 7).type);
        assertEquals(1, playChessService.getPieceAt('g', 7).killScore);
        assertEquals(1, playChessService.getPieces(PlayChessService.Division.White).size());
    }

    @Test
    public void checkMate() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('e', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('b', 8, new King(PlayChessService.Division.Black));
        playChessService.placeNewPiece('a', 1, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('e', 7, new Queen(PlayChessService.Division.White));

        playChessService.tryMove('e', 7, 'd', 7);
        assertEquals(PlayChessService.Division.White, playChessService.getWinner());
    }

    @Test
    public void checkMate2() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('e', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('a', 8, new King(PlayChessService.Division.Black));
        playChessService.placeNewPiece('a', 7, new Bishop(PlayChessService.Division.White));
        playChessService.placeNewPiece('c', 8, new Queen(PlayChessService.Division.White));

        playChessService.tryMove('c', 8, 'b', 8);
        assertEquals(PlayChessService.Division.White, playChessService.getWinner());
    }

    @Test
    public void notCheckMate() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('e', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('b', 8, new King(PlayChessService.Division.Black));
        playChessService.placeNewPiece('a', 1, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('e', 7, new Queen(PlayChessService.Division.White));
        playChessService.placeNewPiece('c', 8, new Bishop(PlayChessService.Division.Black));

        playChessService.tryMove('e', 7, 'd', 7);
        assertNull(playChessService.getWinner());
    }



    @Test
    public void undoMove() {
        playChessService.tryMove('a', 2, 'a', 4);
        playChessService.tryMove('b', 7, 'b', 5);

        playChessService.undoMove(playChessService.getMoves().get(playChessService.getMoves().size() - 1));
        ChessPiece piece = playChessService.getPieceAt('b', 7);

        assertEquals(1, playChessService.getMoves().size());
        assertNull(playChessService.getPieceAt('b', 5));
        assertEquals(ChessPiece.Type.Pawn, piece.type);
        assertEquals(0, piece.moveCount);
    }

    @Test
    public void undoMoveAfterKill() {
        playChessService.tryMove('a', 2, 'a', 4);
        playChessService.tryMove('b', 7, 'b', 5);
        playChessService.tryMove('a', 4, 'b', 5);

        playChessService.undoMove(playChessService.getMoves().get(playChessService.getMoves().size() - 1));
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('a', 4).type);
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('b', 5).type);
        assertEquals(PlayChessService.Division.Black, playChessService.getPieceAt('b', 5).division);
        assertEquals(0, playChessService.getPieceAt('a' ,4).killScore);
        assertEquals(16, playChessService.getPieces(PlayChessService.Division.Black).size());
    }

    @Test
    public void undoLastMoveOfWhite() {
        playChessService.tryMove('a', 2,'a', 4);
        playChessService.tryMove('b', 7, 'b', 5);

        playChessService.undoMoves(PlayChessService.Division.White);
        assertEquals(0, playChessService.getMoves().size());
        assertNull(playChessService.getPieceAt('b', 5));
        assertNull(playChessService.getPieceAt('a', 4));
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('b', 7).type);
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('a', 2).type);
        assertEquals(PlayChessService.Division.White, playChessService.getCurrentTurn());
    }

    @Test
    public void undoLastMovesOfBlackAfterKill() {
        kill_b7PawnWith_a2Pawn();

        playChessService.undoMoves(PlayChessService.Division.Black);
        assertEquals(1, playChessService.getMoves().size());
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('a', 4).type);
        assertEquals(0, playChessService.getPieceAt('a', 4).killScore);
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('b', 7).type);
        assertEquals(PlayChessService.Division.Black, playChessService.getCurrentTurn());
    }


    @Test
    public void undoBlackLastMovesWhenWhiteTurn() {
        playChessService.tryMove('a', 2, 'a', 4);
        playChessService.tryMove('b', 7, 'b', 5);

        playChessService.undoMoves(PlayChessService.Division.Black);
        assertEquals(PlayChessService.Division.Black, playChessService.getCurrentTurn());
    }

    @Test
    public void kingSideCastling() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('e', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('h', 1, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('e', 8, new King(PlayChessService.Division.Black));

        playChessService.tryMove('e', 1, 'g', 1);
        assertEquals(ChessPiece.Type.King, playChessService.getPieceAt('g', 1).getType());
        assertEquals(ChessPiece.Type.Rook, playChessService.getPieceAt('f', 1).getType());
    }

    @Test
    public void queenSideCastling() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('e', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('a', 1, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('e', 8, new King(PlayChessService.Division.Black));

        playChessService.tryMove('e', 1, 'c', 1);
        assertEquals(ChessPiece.Type.King, playChessService.getPieceAt('c', 1).getType());
        assertEquals(ChessPiece.Type.Rook, playChessService.getPieceAt('d', 1).getType());
    }

    @Test
    public void castlingProcess() {
        playChessService.tryMove('e', 2, 'e', 4);
        playChessService.tryMove('e', 7, 'e', 5);
        playChessService.tryMove('f', 1, 'c', 4);
        playChessService.tryMove('f', 8, 'c', 5);
        playChessService.tryMove('g', 1, 'f', 3);
        playChessService.tryMove('g', 8, 'f', 6);

        playChessService.tryMove('e', 1, 'g', 1);

        assertEquals(ChessPiece.Type.King, playChessService.getPieceAt('g', 1).getType());
        assertEquals(ChessPiece.Type.Rook, playChessService.getPieceAt('f', 1).getType());
        assertNull(playChessService.getPieceAt('h', 1));
    }

    @Test
    public void canNotCastlingBecauseCheck() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('e', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('h', 1, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('f', 8, new Rook(PlayChessService.Division.Black));
        playChessService.placeNewPiece('e', 8, new King(PlayChessService.Division.Black));
        playChessService.tryMove('e', 1, 'g', 1);

        assertNull(playChessService.getPieceAt('g', 1));
    }

    @Test
    public void checkByCastling() {
        playChessService.clearChessBoard();
        playChessService.placeNewPiece('e', 1, new King(PlayChessService.Division.White));
        playChessService.placeNewPiece('h', 1, new Rook(PlayChessService.Division.White));
        playChessService.placeNewPiece('f', 8, new King(PlayChessService.Division.Black));
        playChessService.tryMove('e', 1, 'g', 1);

        King blackKing = playChessService.getKing(PlayChessService.Division.Black);

        System.out.print(playChessService.getChessBoard());

        assertTrue(blackKing.isChecked());
    }


    @Test
    public void promotion() {
        promotePawns();
        System.out.println(playChessService.getChessBoard().toString());
        ChessPiece promotedWhitePiece = playChessService.getPieceAt('a', 8);
        assertEquals(ChessPiece.Type.Queen, promotedWhitePiece.getType());
        assertEquals(2, promotedWhitePiece.killScore);
        assertEquals(5, promotedWhitePiece.moveCount);

        ChessPiece promotedBlackPiece = playChessService.getPieceAt('h', 1);
        assertEquals(ChessPiece.Type.Queen, promotedBlackPiece.getType());
        assertEquals(2, promotedBlackPiece.killScore);
        assertEquals(5, promotedBlackPiece.moveCount);
    }

    @Test
    public void undoPromotion() {
        promotePawns();

        playChessService.undoMoves(PlayChessService.Division.White);

        assertNotNull(playChessService.getPieceAt('a', 8));
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('b', 7).type);
        assertNotNull(playChessService.getPieceAt('h', 1));
        assertEquals(ChessPiece.Type.Pawn, playChessService.getPieceAt('g', 2).type);
    }

    private void promotePawns() {
        playChessService.tryMove('a', 2, 'a', 4);
        playChessService.tryMove('h', 7, 'h', 5);
        playChessService.tryMove('a', 4, 'a', 5);
        playChessService.tryMove('h', 5, 'h', 4);
        playChessService.tryMove('a', 5, 'a', 6);
        playChessService.tryMove('h', 4, 'h', 3);
        playChessService.tryMove('a', 6, 'b', 7);
        playChessService.tryMove('h', 3, 'g', 2);
        playChessService.tryMove('b', 7, 'a', 8);
        playChessService.promote(ChessPiece.Type.Queen);
        playChessService.tryMove('g', 2, 'h', 1);
        playChessService.promote(ChessPiece.Type.Queen);
    }

    public void kill_b7PawnWith_a2Pawn() {
        playChessService.tryMove('a', 2, 'a', 4);
        playChessService.tryMove('b', 7, 'b', 5);
        playChessService.tryMove('a', 4, 'b', 5);
    }
}