package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Bishop extends ChessPiece {
    public Bishop(PlayChessService.Division division) {
        super(division);
        type = Type.Bishop;
    }
}
