package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Bishop extends ChessPiece {
    public Bishop(ChessGame.Division division) {
        super(division);
        type = Type.Bishop;
    }
}
