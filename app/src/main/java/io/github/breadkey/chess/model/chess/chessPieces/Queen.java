package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Queen extends ChessPiece {
    public Queen(PlayChessService.Division division) {
        super(division);
        type = Type.Queen;
    }
}
