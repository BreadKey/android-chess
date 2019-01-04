package io.github.breadkey.chess.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public  class ChessBoard {
    public static final List<Character> fileRange = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
    public static final List<Integer> rankRange = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    Square[][] squares;
    public ChessBoard() {
        createSquares();
    }

    private void createSquares() {
        squares = new Square[fileRange.size()][rankRange.size()];

        for (char file: fileRange) {
            for (int rank: rankRange) {
                squares[fileRange.indexOf(file)][rank - 1] = new Square();
            }
        }
    }

    public void placePiece(char file, int rank, ChessPiece piece) {
        if (!rankRange.contains(rank) || !fileRange.contains(file)) {
            return;
        }

        squares[fileRange.indexOf(file)][rank - 1].setPieceOnSquare(piece);
    }

    public ChessPiece getPieceAt(char file, int rank) {
        return squares[fileRange.indexOf(file)][rank - 1].getPieceOnSquare();
    }

    public int countPieces() {
        int count = 0;
        for (char file: fileRange) {
            for (int rank: rankRange) {
                if (squares[fileRange.indexOf(file)][rank - 1].getPieceOnSquare() != null) {
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
