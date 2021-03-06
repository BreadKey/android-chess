package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Pawn extends ChessPiece {
    public Pawn(PlayChessService.Division division) {
        super(division);
        type = Type.Pawn;
    }
}
