package io.github.breadkey.chess.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    private final List<Integer> rankRange = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
    Square[][] squares;
    public ChessBoard() {
        createSquares();
    }

    private void createSquares() {
        squares = new Square[Files.values().length][rankRange.size()];

        for (Files file: Files.values()) {
            for (int rank: rankRange) {
                squares[file.getValue()][rank - 1] = new Square();
            }
        }
    }

    public void placePiece(Files file, int rank, ChessPiece piece) {
        if (!rankRange.contains(rank)) {
            return;
        }

        squares[file.getValue()][rank - 1].setPieceOnSquare(piece);
    }

    public ChessPiece getPieceAt(Files file, int rank) {
        return squares[file.getValue()][rank - 1].getPieceOnSquare();
    }

    public int countPieces() {
        int count = 0;
        for (Files file: Files.values()) {
            for (int rank: rankRange) {
                if (squares[file.getValue()][rank - 1].getPieceOnSquare() != null) {
                    count++;
                }
            }
        }

        return count;
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
