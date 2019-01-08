package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Queen extends ChessPiece {
    public Queen(ChessGame.Division division) {
        super(division);
        type = Type.Queen;
    }
}
