package io.github.breadkey.chess.model.chess;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MoveTest {
    @Test
    public void toStringKingSideCastling() {
        Move move = new Move(PlayChessService.Division.White, ChessPiece.Type.King, new Coordinate('e', 1), new Coordinate('g', 1));
        move.setRules(Arrays.asList(ChessRuleManager.Rule.KingSideCastling));

        assertEquals("0-0", move.toString());
    }

    @Test
    public void toStringQueenSideCastling() {
        Move move = new Move(PlayChessService.Division.White, ChessPiece.Type.King, new Coordinate('e', 1), new Coordinate('c', 1));
        move.setRules(Arrays.asList(ChessRuleManager.Rule.QueenSideCastling));

        assertEquals("0-0-0", move.toString());
    }

    @Test
    public void toStringCheckByKnight() {
        Move move = new Move(PlayChessService.Division.White, ChessPiece.Type.Knight, new Coordinate('c', 4), new Coordinate('d', 6));
        move.setRules(Arrays.asList(ChessRuleManager.Rule.Check));

        assertEquals("Nd6+", move.toString());
    }
}