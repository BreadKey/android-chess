package io.github.breadkey.chess.model.chess;

public abstract class ChessPiece {
    public int moveCount = 0;
    public int killScore = 0;
    private char file;
    private  int rank;

    public enum Type {
        King,
        Queen,
        Rook,
        Bishop,
        Knight,
        Pawn
    }

    public ChessPiece(ChessGame.Division division) {
        this.division = division;
    }

    public Type type;
    public ChessGame.Division division;

    public Type getType() {
        return type;
    }

    public void setCoordinate(char file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public char getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }
}
