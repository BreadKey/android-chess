package io.github.breadkey.chess.model.chessPieces;

import io.github.breadkey.chess.model.ChessGame;
import io.github.breadkey.chess.model.ChessPiece;

public class Knight extends ChessPiece {
    public Knight(ChessGame.Division division) {
        super(division);
        type = Type.Knight;
    }
}
