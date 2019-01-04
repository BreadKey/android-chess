package io.github.breadkey.chess.model.chessPieces;

import io.github.breadkey.chess.model.ChessGame;
import io.github.breadkey.chess.model.ChessPiece;

public class Bishop extends ChessPiece {
    public Bishop(ChessGame.Division division) {
        super(division);
        type = Type.Bishop;
    }
}
