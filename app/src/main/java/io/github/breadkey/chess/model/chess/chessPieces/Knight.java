package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Knight extends ChessPiece {
    public Knight(ChessGame.Division division) {
        super(division);
        type = Type.Knight;
    }
}
