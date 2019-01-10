package io.github.breadkey.chess.model.chess;

public abstract class ChessPiece {
    public int moveCount = 0;
    public int killScore = 0;
    private Coordinate coordinate;
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
        coordinate = new Coordinate(file, rank);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public char getFile() {
        return coordinate.getFile();
    }

    public int getRank() {
        return coordinate.getRank();
    }
}
