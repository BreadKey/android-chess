package io.github.breadkey.chess.model.chessPieces;

import io.github.breadkey.chess.model.ChessGame;
import io.github.breadkey.chess.model.ChessPiece;

public class King extends ChessPiece {
    public King(ChessGame.Division division) {
        super(division);
        type = Type.King;
    }
}
