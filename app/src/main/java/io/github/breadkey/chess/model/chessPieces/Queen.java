package io.github.breadkey.chess.model.chessPieces;

import io.github.breadkey.chess.model.ChessGame;
import io.github.breadkey.chess.model.ChessPiece;

public class Queen extends ChessPiece {
    public Queen(ChessGame.Division division) {
        super(division);
        type = Type.Queen;
    }
}
