package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Knight extends ChessPiece {
    public Knight(PlayChessService.Division division) {
        super(division);
        type = Type.Knight;
    }
}
