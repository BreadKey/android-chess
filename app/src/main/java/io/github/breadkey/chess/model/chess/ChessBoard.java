package io.github.breadkey.chess.model.chess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.github.breadkey.chess.model.chess.chessPieces.King;

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
        if (piece != null) {
            piece.setCoordinate(file, rank);
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

    static boolean isOutOfBoard(char file, int rank) {
        if (file < files.get(0) || file > files.get(files.size() - 1)) {
            return true;
        }
        return rank > ranks.get(0) || rank < ranks.get(ranks.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder chessBoard = new StringBuilder();

        for(int rank: ranks) {
            chessBoard.append(String.valueOf(rank) + "\t");
            for(char file: files) {
                ChessPiece piece = getPieceAt(file, rank);
                if (piece != null) {
                    chessBoard .append(piece.type.toString());
                }
                else {
                    chessBoard.append("\t");
                }
                chessBoard.append("\t");
            }
            chessBoard.append("\n");
        }
        for(char file: files) {
            chessBoard.append("\t" + file + "\t");
        }

        return chessBoard.toString();
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