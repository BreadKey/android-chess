package io.github.breadkey.chess.model;

import java.util.List;

import io.github.breadkey.chess.model.chessPieces.Bishop;
import io.github.breadkey.chess.model.chessPieces.King;
import io.github.breadkey.chess.model.chessPieces.Knight;
import io.github.breadkey.chess.model.chessPieces.Pawn;
import io.github.breadkey.chess.model.chessPieces.Queen;
import io.github.breadkey.chess.model.chessPieces.Rook;

public class ChessGame {
    public enum Division {
        Black, White
    }

    ChessBoard chessBoard;
    private Division currentTurn;
    private ChessRuleManager ruleManager = ChessRuleManager.getInstance();

    public ChessGame() {
        chessBoard = new ChessBoard();

        placePieces();
        currentTurn = Division.White;
    }

    private void placePieces() {
        placePawns();
        placeRooks();
        placeKnights();
        placeBishops();
        placeQueens();
        placeKings();
    }

    private void placePawns() {
        for (char file: ChessBoard.files) {
            chessBoard.placePiece(file, 2, new Pawn(Division.White));
            chessBoard.placePiece(file, 7, new Pawn(Division.Black));
        }
    }

    private void placeRooks() {
        chessBoard.placePiece('a', 1, new Rook(Division.White));
        chessBoard.placePiece('h', 1, new Rook(Division.White));
        chessBoard.placePiece('a', 8, new Rook(Division.Black));
        chessBoard.placePiece('h', 8, new Rook(Division.Black));
    }

    private void placeKnights() {
        chessBoard.placePiece('b', 1, new Knight(Division.White));
        chessBoard.placePiece('g', 1, new Knight(Division.White));
        chessBoard.placePiece('b', 8, new Knight(Division.Black));
        chessBoard.placePiece('g', 8, new Knight(Division.Black));
    }

    private void placeBishops() {
        chessBoard.placePiece('c', 1, new Bishop(Division.White));
        chessBoard.placePiece('f', 1, new Bishop(Division.White));
        chessBoard.placePiece('c', 8, new Bishop(Division.Black));
        chessBoard.placePiece('f', 8, new Bishop(Division.Black));
    }

    private void placeQueens() {
        chessBoard.placePiece('d', 1, new Queen(Division.White));
        chessBoard.placePiece('d', 8, new Queen(Division.Black));
    }

    private void placeKings() {
        chessBoard.placePiece('e', 1, new King(Division.White));
        chessBoard.placePiece('e', 8, new King(Division.Black));
    }

    public ChessPiece getPieceAt(char file, int rank) {
        return chessBoard.getPieceAt(file, rank);
    }

    public void move(char fromFile, int fromRank, char toFile, int toRank) {
        List coordinatesCanMove = ruleManager.findSquareCoordinateCanMove(chessBoard, fromFile, fromRank);
        if (isCoordinatesContain(coordinatesCanMove, toFile, toRank)) {
            ChessPiece pieceWillMove = getPieceAt(fromFile, fromRank);
            if (getPieceAt(toFile, toRank) != null) {
                pieceWillMove.killScore++;
            }

            chessBoard.placePiece(toFile, toRank, pieceWillMove);
            chessBoard.placePiece(fromFile, fromRank, null);
            pieceWillMove.moveCount++;

            changeTurn();
        }
    }

    private boolean isCoordinatesContain(List<Coordinate> coordinates, char file, int rank) {
        boolean isContain = false;

        for (Coordinate coordinate: coordinates) {
            if (coordinate.getFile() == file && coordinate.getRank() == rank) {
                isContain = true;
                break;
            }
        }

        return isContain;
    }

    private void changeTurn() {
        if (currentTurn == Division.Black) {
            currentTurn = Division.White;
        }
        else {
            currentTurn = Division.Black;
        }
    }

    public Division getCurrentTurn() {
        return currentTurn;
    }
}
