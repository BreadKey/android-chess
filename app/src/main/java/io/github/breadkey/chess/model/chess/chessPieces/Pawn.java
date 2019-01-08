package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Pawn extends ChessPiece {
    public Pawn(ChessGame.Division division) {
        super(division);
        type = Type.Pawn;
    }
}
