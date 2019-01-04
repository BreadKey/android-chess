package io.github.breadkey.chess.model.chessPieces;

import io.github.breadkey.chess.model.ChessGame;
import io.github.breadkey.chess.model.ChessPiece;

public class Rook extends ChessPiece {
    public Rook(ChessGame.Division division) {
        super(division);
        type = Type.Rook;
    }
}
