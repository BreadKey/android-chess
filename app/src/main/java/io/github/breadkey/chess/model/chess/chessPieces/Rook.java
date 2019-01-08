package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class Rook extends ChessPiece {
    public Rook(ChessGame.Division division) {
        super(division);
        type = Type.Rook;
    }
}
