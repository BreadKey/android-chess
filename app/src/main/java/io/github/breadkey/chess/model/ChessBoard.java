package io.github.breadkey.chess.model;

import java.util.Arrays;
import java.util.List;

public  class ChessBoard {
    public static final List<Character> files = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
    public static final List<Integer> ranks = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);
    Square[][] squares;
    public ChessBoard() {
        createSquares();
    }

    private void createSquares() {
        squares = new Square[files.size()][ranks.size()];

        for (char file: files) {
            for (int rank: ranks) {
                squares[files.indexOf(file)][rank - 1] = new Square();
            }
        }
    }

    public void placePiece(char file, int rank, ChessPiece piece) {
        if (isOutOfBoard(file, rank)) {
            return;
        }

        squares[files.indexOf(file)][rank - 1].setPieceOnSquare(piece);
    }

    public ChessPiece getPieceAt(char file, int rank) {
        if (isOutOfBoard(file, rank)) {
            return null;
        }

        return squares[files.indexOf(file)][rank - 1].getPieceOnSquare();
    }

    public int countPieces() {
        int count = 0;
        for (char file: files) {
            for (int rank: ranks) {
                if (squares[files.indexOf(file)][rank - 1].getPieceOnSquare() != null) {
                    count++;
                }
            }
        }

        return count;
    }

    boolean isOutOfBoard(char file, int rank) {
        return !files.contains(file) || !ranks.contains(rank);
    }
}

class Square {
    private ChessPiece pieceOnSquare;

    void setPieceOnSquare(ChessPiece pieceOnSquare) {
        this.pieceOnSquare = pieceOnSquare;
    }

    ChessPiece getPieceOnSquare() {
        return pieceOnSquare;
    }
}

class Coordinate {
    private char file;
    private int rank;

    Coordinate(char file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    char getFile() {
        return file;
    }

    int getRank() {
        return rank;
    }
}