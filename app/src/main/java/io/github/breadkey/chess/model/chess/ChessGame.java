package io.github.breadkey.chess.model.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.breadkey.chess.model.ChessGameObserver;
import io.github.breadkey.chess.model.chess.chessPieces.Bishop;
import io.github.breadkey.chess.model.chess.chessPieces.King;
import io.github.breadkey.chess.model.chess.chessPieces.Knight;
import io.github.breadkey.chess.model.chess.chessPieces.Pawn;
import io.github.breadkey.chess.model.chess.chessPieces.Queen;
import io.github.breadkey.chess.model.chess.chessPieces.Rook;

public class ChessGame {
    public enum Division {
        Black, White
    }

    private ChessBoard chessBoard;
    HashMap<Division, King> kingHashMap;
    HashMap<Division, List<ChessPiece>> piecesHashMap;

    private Division currentTurn;
    private ChessRuleManager ruleManager = ChessRuleManager.getInstance();
    private List<ChessGameObserver> gameObservers;

    public ChessGame() {
        chessBoard = new ChessBoard();
        gameObservers = new ArrayList<>();
        kingHashMap = new HashMap<>();
        piecesHashMap = new HashMap<>();
        piecesHashMap.put(Division.White, new ArrayList<ChessPiece>());
        piecesHashMap.put(Division.Black, new ArrayList<ChessPiece>());

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
            placeNewPiece(file, 2, new Pawn(Division.White));
            placeNewPiece(file, 7, new Pawn(Division.Black));
        }
    }

    private void placeRooks() {
        placeNewPiece('a', 1, new Rook(Division.White));
        placeNewPiece('h', 1, new Rook(Division.White));
        placeNewPiece('a', 8, new Rook(Division.Black));
        placeNewPiece('h', 8, new Rook(Division.Black));
    }

    private void placeKnights() {
        placeNewPiece('b', 1, new Knight(Division.White));
        placeNewPiece('g', 1, new Knight(Division.White));
        placeNewPiece('b', 8, new Knight(Division.Black));
        placeNewPiece('g', 8, new Knight(Division.Black));
    }

    private void placeBishops() {
        placeNewPiece('c', 1, new Bishop(Division.White));
        placeNewPiece('f', 1, new Bishop(Division.White));
        placeNewPiece('c', 8, new Bishop(Division.Black));
        placeNewPiece('f', 8, new Bishop(Division.Black));
    }

    private void placeQueens() {
        placeNewPiece('d', 1, new Queen(Division.White));
        placeNewPiece('d', 8, new Queen(Division.Black));
    }

    private void placeKings() {
        placeNewPiece('e', 1, new King(Division.White));
        placeNewPiece('e', 8, new King(Division.Black));
    }

    public ChessPiece getPieceAt(char file, int rank) {
        return chessBoard.getPieceAt(file, rank);
    }

    public void move(char fromFile, int fromRank, char toFile, int toRank) {
        ChessPiece pieceWillMove = getPieceAt(fromFile, fromRank);
        List coordinatesCanMove = ruleManager.findSquareCoordinateCanMove(chessBoard, fromFile, fromRank);

        if (isCoordinatesContain(coordinatesCanMove, toFile, toRank)) {
            pieceWillMove = getPieceAt(fromFile, fromRank);

            ChessPiece pieceWillDead =  getPieceAt(toFile, toRank);
            if (pieceWillDead != null) {
                pieceWillMove.killScore++;
                piecesHashMap.get(pieceWillDead.division).remove(pieceWillDead);
                for (ChessGameObserver gameObserver : gameObservers) {
                    gameObserver.killHappened(pieceWillMove, pieceWillDead, toFile, toRank);
                }
            }

            chessBoard.placePiece(toFile, toRank, pieceWillMove);
            chessBoard.placePiece(fromFile, fromRank, null);
            pieceWillMove.moveCount++;

            changeTurn();
            if (isCheck(pieceWillMove, toFile, toRank)) {

            }
            for (ChessGameObserver gameObserver : gameObservers) {
                gameObserver.pieceMoved(fromFile, fromRank, toFile, toRank, pieceWillMove);
            }

            return;
        }

        for (ChessGameObserver gameObserver : gameObservers) {
            gameObserver.canNotMoveThatCoordinates(fromFile, toFile, fromRank, toRank);
        }
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
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

    private boolean isCheck(ChessPiece movedPiece, char movedFile, int movedRank) {
        List<Coordinate> coordinatesCanMoveNextTurn = ruleManager.findSquareCoordinateCanMove(chessBoard, movedFile, movedRank);
        King king = movedPiece.division == Division.White ? kingHashMap.get(Division.Black) : kingHashMap.get(Division.White);
        if (isCoordinatesContain(coordinatesCanMoveNextTurn, king.getFile(), king.getRank())) {
            king.setChecked(true);
            return true;
        }

        return false;
    }

    public Division getCurrentTurn() {
        return currentTurn;
    }

    public void attachGameObserver(ChessGameObserver gameObserver) {
        if (!gameObservers.contains(gameObserver)) {
            gameObservers.add(gameObserver);
        }
    }

    public void detachGameObserver(ChessGameObserver gameObserver) {
        gameObservers.remove(gameObserver);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(chessBoard);
        stringBuilder.append("\n현재 차례: " + currentTurn);

        return stringBuilder.toString();
    }

    public void clearChessBoard() {
        chessBoard = new ChessBoard();
        kingHashMap.clear();
        piecesHashMap.get(Division.White).clear();
        piecesHashMap.get(Division.Black).clear();
    }

    public void placeNewPiece(char file, int rank, ChessPiece piece) {
        if (piece.type == ChessPiece.Type.King) {
            kingHashMap.put(piece.division, (King) piece);
        }

        Division pieceDivision = piece.division;
        piecesHashMap.get(pieceDivision).add(piece);

        chessBoard.placePiece(file, rank, piece);
    }
}
