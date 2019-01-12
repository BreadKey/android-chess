package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Rook extends ChessPiece {
    public Rook(PlayChessService.Division division) {
        super(division);
        type = Type.Rook;
    }
}
