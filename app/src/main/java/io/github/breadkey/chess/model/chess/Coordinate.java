package io.github.breadkey.chess.model.chess;

public class Coordinate {
    private char file;
    private int rank;

    public Coordinate(char file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public char getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return getFile() + String.valueOf(getRank());
    }
}
