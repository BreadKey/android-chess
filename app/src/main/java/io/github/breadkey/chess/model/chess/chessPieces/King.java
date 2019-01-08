package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class King extends ChessPiece {
    public King(ChessGame.Division division) {
        super(division);
        type = Type.King;
    }
}
