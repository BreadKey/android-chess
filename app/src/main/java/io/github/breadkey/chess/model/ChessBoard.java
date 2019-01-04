package io.github.breadkey.chess.model;

import java.util.HashMap;

public  class ChessBoard {
    public enum Files {
        a(0), b(1), c(2), d(3), e(4), f(5), g(6), h(7);

        private int value;
        private Files(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final int[] rankRange = {1, 2, 3, 4, 5, 6, 7};

    Square[][] squares;
    public ChessBoard() {
        createSquares();
    }

    private void createSquares() {
        squares = new Square[Files.values().length][rankRange.length];

        for (Files file: Files.values()) {
            for (int rank: rankRange) {
                squares[file.getValue()][rank - 1] = new Square();
            }
        }
    }

    public void placePiece(Files file, int rank, ChessPiece piece) {
        squares[file.getValue()][rank - 1].setPieceOnSquare(piece);
    }

    public ChessPiece getPieceAt(Files file, int rank) {
        return squares[file.getValue()][rank - 1].getPieceOnSquare();
    }
}

class Square {
    private ChessPiece pieceOnSquare;

    public void setPieceOnSquare(ChessPiece pieceOnSquare) {
        this.pieceOnSquare = pieceOnSquare;
    }

    public ChessPiece getPieceOnSquare() {
        return pieceOnSquare;
    }
}
