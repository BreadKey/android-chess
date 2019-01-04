package io.github.breadkey.chess.model;

public abstract class ChessPiece {
    public enum Type {
        King,
        Queen,
        Rook,
        Bishop,
        Knight,
        Pawn
    }

    public Type type;

    public Type getType() {
        return type;
    }
}
