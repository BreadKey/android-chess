package io.github.breadkey.chess.model.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.github.breadkey.chess.model.chess.chessPieces.Bishop;
import io.github.breadkey.chess.model.chess.chessPieces.King;
import io.github.breadkey.chess.model.chess.chessPieces.Knight;
import io.github.breadkey.chess.model.chess.chessPieces.Pawn;
import io.github.breadkey.chess.model.chess.chessPieces.Queen;
import io.github.breadkey.chess.model.chess.chessPieces.Rook;

public  class ChessBoard {
    public static final List<Character> files = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
    public static final List<Integer> ranks = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);
    private HashMap<PlayChessService.Division, King> kingHashMap;
    private HashMap<PlayChessService.Division, List<ChessPiece>> piecesHashMap;
    HashMap<Coordinate, Square> squares;

    public ChessBoard() {
        createSquares();
        kingHashMap = new HashMap<>();
        piecesHashMap = new HashMap<>();
        piecesHashMap.put(PlayChessService.Division.White, new ArrayList<ChessPiece>(16));
        piecesHashMap.put(PlayChessService.Division.Black, new ArrayList<ChessPiece>(16));
    }

    private void createSquares() {
        squares = new HashMap<>();

        for (char file: files) {
            for (int rank: ranks) {
                squares.put(new Coordinate(file, rank), new Square());
            }
        }
    }

    public void setChessBoard() {
        placePawns();
        placeRooks();
        placeKnights();
        placeBishops();
        placeQueens();
        placeKings();
    }

    private void placePawns() {
        for (char file: ChessBoard.files) {
            placeNewPiece(file, 2, new Pawn(PlayChessService.Division.White));
            placeNewPiece(file, 7, new Pawn(PlayChessService.Division.Black));
        }
    }

    private void placeRooks() {
        placeNewPiece('a', 1, new Rook(PlayChessService.Division.White));
        placeNewPiece('h', 1, new Rook(PlayChessService.Division.White));
        placeNewPiece('a', 8, new Rook(PlayChessService.Division.Black));
        placeNewPiece('h', 8, new Rook(PlayChessService.Division.Black));
    }

    private void placeKnights() {
        placeNewPiece('b', 1, new Knight(PlayChessService.Division.White));
        placeNewPiece('g', 1, new Knight(PlayChessService.Division.White));
        placeNewPiece('b', 8, new Knight(PlayChessService.Division.Black));
        placeNewPiece('g', 8, new Knight(PlayChessService.Division.Black));
    }

    private void placeBishops() {
        placeNewPiece('c', 1, new Bishop(PlayChessService.Division.White));
        placeNewPiece('f', 1, new Bishop(PlayChessService.Division.White));
        placeNewPiece('c', 8, new Bishop(PlayChessService.Division.Black));
        placeNewPiece('f', 8, new Bishop(PlayChessService.Division.Black));
    }

    private void placeQueens() {
        placeNewPiece('d', 1, new Queen(PlayChessService.Division.White));
        placeNewPiece('d', 8, new Queen(PlayChessService.Division.Black));
    }

    private void placeKings() {
        placeNewPiece('e', 1, new King(PlayChessService.Division.White));
        placeNewPiece('e', 8, new King(PlayChessService.Division.Black));
    }

    public void placePiece(char file, int rank, ChessPiece piece) {
        if (isOutOfBoard(file, rank)) {
            return;
        }
        if (piece != null) {
            piece.setCoordinate(file, rank);
        }
        squares.get(new Coordinate(file, rank)).setPieceOnSquare(piece);
    }

    public ChessPiece getPieceAt(char file, int rank) {
        if (isOutOfBoard(file, rank)) {
            return null;
        }

        return squares.get(new Coordinate(file, rank)).getPieceOnSquare();
    }

    public int countPieces() {
        int count = 0;
        for (char file: files) {
            for (int rank: ranks) {
                if (squares.get(new Coordinate(file, rank)).getPieceOnSquare() != null) {
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

    public void placeNewPiece(char file, int rank, ChessPiece piece) {
        if (piece.type == ChessPiece.Type.King) {
            kingHashMap.put(piece.division, (King) piece);
        }

        PlayChessService.Division pieceDivision = piece.division;
        piecesHashMap.get(pieceDivision).add(piece);

        placePiece(file, rank, piece);
    }

    public List<ChessPiece> getPieces(PlayChessService.Division enemyDivision) {
        return piecesHashMap.get(enemyDivision);
    }

    public King getKing(PlayChessService.Division enemyDivision) {
        return kingHashMap.get(enemyDivision);
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