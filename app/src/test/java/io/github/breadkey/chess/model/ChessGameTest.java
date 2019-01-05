package io.github.breadkey.chess.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.github.breadkey.chess.model.chessPieces.Pawn;

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
        assertEquals(null, chessGame.getPieceAt('a', 2));
        assertEquals(ChessGame.Division.Black, chessGame.getCurrentTurn());
        printChessBoard();
    }

    @Test
    public void move_a2PawnTo_a5WhichCanNotMove() {
        chessGame.move('a',2, 'a', 5);
        assertEquals(null, chessGame.getPieceAt('a', 5));
        assertEquals(ChessPiece.Type.Pawn, chessGame.getPieceAt('a', 2).type);
        assertEquals(ChessGame.Division.White, chessGame.getCurrentTurn());
    }

    @Test
    public void move_a5To_a6WhichNullPiece() {
        chessGame.move('a', 5, 'a', 6);
        assertEquals(null, chessGame.getPieceAt('a', 5));
        assertEquals(null, chessGame.getPieceAt('a', 6));
        assertEquals(ChessGame.Division.White, chessGame.getCurrentTurn());
    }

    private void printChessBoard() {
        for(int rank: ChessBoard.ranks) {
            System.out.print(String.valueOf(rank) + "\t");
            for(char file: ChessBoard.files) {
                ChessPiece piece = chessGame.getPieceAt(file, rank);
                if (piece != null) {
                    System.out.print(piece.type.toString());
                }
                else {
                    System.out.print('\t');
                }
                System.out.print('\t');
            }
            System.out.println();
        }
        for(char file: ChessBoard.files) {
            System.out.print("\t" + file + "\t");
        }
        System.out.println();
    }
}