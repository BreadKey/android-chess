package io.github.breadkey.chess.model.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.breadkey.chess.model.chess.chessPieces.King;

public class ChessRuleManager {
    private static final ChessRuleManager ourInstance = new ChessRuleManager();
    public enum Rule {
        Check,
        Checkmate,
        KingSideCastling,
        QueenSideCastling,
        EnPassant
    }

    public static ChessRuleManager getInstance() {
        return ourInstance;
    }

    private ChessRuleManager() {
    }

    public List<Coordinate> findSquareCoordinateCanMove(ChessBoard chessBoard, char file, int rank) {
        ChessPiece piece = chessBoard.getPieceAt(file, rank);
        if (piece == null) {
            return new ArrayList<>();
        }

        List<Coordinate> coordinates = new ArrayList<>();
        PlayChessService.Division division = piece.division;

        switch (piece.type) {
            case Pawn: {
                int forward = 1;
                if (division == PlayChessService.Division.Black) {
                    forward = -1;
                }
                List<Coordinate> forwardLine = new ArrayList<>();
                forwardLine.add(new Coordinate(file, rank + forward));
                if (piece.moveCount == 0) {
                    forwardLine.add(new Coordinate(file, rank + forward * 2));
                }
                forwardLine = filterOutOfBoard(forwardLine);
                findCoordinatesInStraightLine(chessBoard, coordinates, forwardLine, division);

                for (Coordinate anotherPiecePlace: findAnotherPiecePlace(chessBoard, forwardLine)) {
                    coordinates.remove(anotherPiecePlace);
                }

                List<Coordinate> sideForwardCoordinates = new ArrayList<>();
                sideForwardCoordinates.add(new Coordinate((char) (file - 1), rank + forward));
                sideForwardCoordinates.add(new Coordinate((char) (file + 1), rank + forward));

                sideForwardCoordinates = filterOutOfBoard(sideForwardCoordinates);
                sideForwardCoordinates = findAnotherPiecePlace(chessBoard, sideForwardCoordinates);
                for (Coordinate sideForwardCoordinate: sideForwardCoordinates) {
                    ChessPiece anotherPiece = chessBoard.getPieceAt(sideForwardCoordinate.getFile(), sideForwardCoordinate.getRank());
                    if (anotherPiece.division != division) {
                        coordinates.add(sideForwardCoordinate);
                    }
                }

                break;
            }

            case Knight: {
                coordinates.add(new Coordinate((char) (file - 1), rank + 2));
                coordinates.add(new Coordinate((char) (file + 1), rank + 2));
                coordinates.add(new Coordinate((char) (file - 1), rank - 2));
                coordinates.add(new Coordinate((char) (file + 1), rank - 2));
                coordinates.add(new Coordinate((char) (file - 2), rank + 1));
                coordinates.add(new Coordinate((char) (file - 2), rank - 1));
                coordinates.add(new Coordinate((char) (file + 2), rank + 1));
                coordinates.add(new Coordinate((char) (file + 2), rank - 1));

                coordinates = filterOutOfBoard(coordinates);
                coordinates = filterAllyPiecePlaced(chessBoard, coordinates, division);
                break;
            }

            case Rook: {
                findRookCoordinatesCanMove(chessBoard, coordinates, file, rank, division);
                break;
            }

            case Bishop: {
                findBishopCoordinatesCanMove(chessBoard, coordinates, file, rank, division);
                break;
            }

            case Queen: {
                findRookCoordinatesCanMove(chessBoard, coordinates, file, rank, division);
                findBishopCoordinatesCanMove(chessBoard, coordinates, file, rank, division);
                break;
            }

            case King: {
                coordinates.add(new Coordinate((char) (file - 1), rank));
                coordinates.add(new Coordinate((char) (file - 1), rank + 1));
                coordinates.add(new Coordinate(file, rank + 1));
                coordinates.add(new Coordinate((char) (file + 1), rank + 1));
                coordinates.add(new Coordinate((char) (file + 1), rank));
                coordinates.add(new Coordinate((char) (file + 1), rank - 1));
                coordinates.add(new Coordinate(file, rank - 1));
                coordinates.add(new Coordinate((char) (file + -1), rank - 1));

                coordinates = filterOutOfBoard(coordinates);
                coordinates = filterAllyPiecePlaced(chessBoard, coordinates, division);

                addCastlingCoordinate(chessBoard, piece, file, rank, coordinates);

                break;
            }
        }

        return coordinates;
    }

    private void addCastlingCoordinate(ChessBoard chessBoard, ChessPiece king, char file, int rank, List<Coordinate> destination) {
        if (king.moveCount == 0) {
            char kingSideRookFile = ChessBoard.files.get(ChessBoard.files.size() - 1);
            ChessPiece kingSideRook = chessBoard.getPieceAt(kingSideRookFile, rank);
            char queenSideRookFile = ChessBoard.files.get(0);
            ChessPiece queenSideRook = chessBoard.getPieceAt(queenSideRookFile, rank);
            List<Coordinate> lineCoordinates = new ArrayList<>();
            PlayChessService.Division enemyDivision = king.division == PlayChessService.Division.White ? PlayChessService.Division.Black : PlayChessService.Division.White;
            findRookCoordinatesCanMove(chessBoard, lineCoordinates, file, rank, enemyDivision);

            ChessPiece[] rooks = new ChessPiece[]{kingSideRook, queenSideRook};

            for (ChessPiece rook : rooks) {
                if (rook != null && rook.moveCount == 0) {
                    if (lineCoordinates.contains(rook.getCoordinate())) {
                        int side = rook.getFile() > file? 1 : -1;
                        if (!arePiecesCanMove(chessBoard, enemyDivision, new Coordinate((char) (file + side), rank))) {
                            destination.add(new Coordinate((char) (file + side * 2), rank));
                        }
                    }
                }
            }
        }
    }

    private List<Coordinate> findAnotherPiecePlace(ChessBoard chessBoard, List<Coordinate> coordinates) {
        List<Coordinate> anotherPiecePlaceCoordinates = new ArrayList<>();
        for (Coordinate coordinate: coordinates) {
            if (chessBoard.getPieceAt(coordinate.getFile(), coordinate.getRank()) != null) {
                anotherPiecePlaceCoordinates.add(coordinate);
            }
        }

        return anotherPiecePlaceCoordinates;
    }

    private List<Coordinate> filterOutOfBoard(List<Coordinate> coordinates) {
        List<Coordinate> filteredCoordinates = new ArrayList<>(coordinates);

        for (Coordinate coordinate: coordinates) {
            if (ChessBoard.isOutOfBoard(coordinate.getFile(), coordinate.getRank())) {
                filteredCoordinates.remove(coordinate);
            }
        }

        return filteredCoordinates;
    }

    private List<Coordinate> filterAllyPiecePlaced(ChessBoard chessBoard, List<Coordinate> coordinates, PlayChessService.Division division) {
        List<Coordinate> filteredCoordinates = new ArrayList<>(coordinates);

        for (Coordinate anotherPiecePlace: findAnotherPiecePlace(chessBoard, coordinates)) {
            if (chessBoard.getPieceAt(anotherPiecePlace.getFile(), anotherPiecePlace.getRank()).division == division) {
                filteredCoordinates.remove(anotherPiecePlace);
            }
        }

        return filteredCoordinates;
    }

    private void findRookCoordinatesCanMove(ChessBoard chessBoard, List<Coordinate> destination, char file, int rank, PlayChessService.Division division) {
        int pieceFileIndex = ChessBoard.files.indexOf(file);
        int fileCount = ChessBoard.files.size();
        int pieceRankIndex = ChessBoard.ranks.indexOf(rank);
        int rankCount = ChessBoard.ranks.size();

        List<Coordinate> leftLine = new ArrayList<>();
        for (char leftFile: ChessBoard.files.subList(0, pieceFileIndex)) {
            leftLine.add(new Coordinate(leftFile, rank));
        }
        Collections.reverse(leftLine);
        findCoordinatesInStraightLine(chessBoard, destination, leftLine, division);

        List<Coordinate> rightLine = new ArrayList<>();
        for (char rightFile: ChessBoard.files.subList(pieceFileIndex + 1, fileCount)) {
            rightLine.add(new Coordinate(rightFile, rank));
        }
        findCoordinatesInStraightLine(chessBoard, destination, rightLine, division);

        List<Coordinate> upLine = new ArrayList<>();
        for (int upRank: ChessBoard.ranks.subList(0, pieceRankIndex)) {
            upLine.add(new Coordinate(file, upRank));
        }
        Collections.reverse(upLine);
        findCoordinatesInStraightLine(chessBoard, destination, upLine, division);

        List<Coordinate> downLine = new ArrayList<>();
        for (int downRank: ChessBoard.ranks.subList(pieceRankIndex + 1, rankCount)) {
            downLine.add(new Coordinate(file, downRank));
        }
        findCoordinatesInStraightLine(chessBoard, destination, downLine, division);
    }

    private void findBishopCoordinatesCanMove(ChessBoard chessBoard, List<Coordinate> destination, char file, int rank, PlayChessService.Division division) {
        List<Coordinate> leftUpLine = findDiagonalCoordinates(file, rank, -1 , 1);
        findCoordinatesInStraightLine(chessBoard, destination, leftUpLine, division);

        List<Coordinate> rightDownLine = findDiagonalCoordinates(file, rank, 1, -1);
        findCoordinatesInStraightLine(chessBoard, destination, rightDownLine, division);

        List<Coordinate> leftDownLine = findDiagonalCoordinates(file, rank, -1, -1);
        findCoordinatesInStraightLine(chessBoard, destination, leftDownLine, division);

        List<Coordinate> rightUpLine = findDiagonalCoordinates(file, rank, 1, 1);
        findCoordinatesInStraightLine(chessBoard, destination, rightUpLine, division);
    }

    private List<Coordinate> findDiagonalCoordinates(char atFile, int atRank, int fileDirection, int rankDirection) {
        List<Coordinate> diagonalCoordinates = new ArrayList<>();
        char currentFile = atFile;
        int currentRank = atRank;

        while (true) {
            currentFile += fileDirection;
            currentRank += rankDirection;
            if (ChessBoard.isOutOfBoard(currentFile, currentRank)) {
                break;
            }
            diagonalCoordinates.add(new Coordinate(currentFile, currentRank));
        }

        return diagonalCoordinates;
    }

    private void findCoordinatesInStraightLine(ChessBoard chessBoard, List<Coordinate> destination, List<Coordinate> straightLine, PlayChessService.Division division) {
        for (Coordinate coordinate: straightLine) {
            ChessPiece pieceAlreadyPlaced = chessBoard.getPieceAt(coordinate.getFile(), coordinate.getRank());
            if (pieceAlreadyPlaced != null) {
                if (pieceAlreadyPlaced.division != division) {
                    destination.add(coordinate);
                }
                break;
            }
            destination.add(coordinate);
        }
    }

    public List<Rule> findRules(ChessBoard chessBoard, Move newMove) {
        List<Rule> rules = new ArrayList<>();
        Coordinate fromCoordinate = newMove.getFromCoordinate();
        Coordinate toCoordinate = newMove.getToCoordinate();
        int moveDistance = Math.abs(toCoordinate.getFile() - fromCoordinate.getFile()) + Math.abs(toCoordinate.getRank() - fromCoordinate.getRank());

        if (newMove.getPieceType() == ChessPiece.Type.King) {
            if (moveDistance == 2) {
                if (toCoordinate.getFile() > fromCoordinate.getFile()) {
                    rules.add(Rule.KingSideCastling);
                }
                else {
                    rules.add(Rule.QueenSideCastling);
                }
            }
        }

        PlayChessService.Division enemyDivision = newMove.getDivision() == PlayChessService.Division.White? PlayChessService.Division.Black : PlayChessService.Division.White;
        if (isCheckmate(chessBoard, enemyDivision)) {
            rules.add(Rule.Checkmate);
        }

        else if (isCheck(chessBoard, newMove, enemyDivision)) {
            rules.add(Rule.Check);
        }

        return rules;
    }

    private boolean isCheck(ChessBoard chessBoard, Move newMove, PlayChessService.Division enemyDivision) {
        Coordinate movedCoordinate = newMove.getToCoordinate();
        List<Coordinate> coordinatesCanMoveNextTurn = findSquareCoordinateCanMove(chessBoard, movedCoordinate.getFile(), movedCoordinate.getRank());
        King enemyKing = chessBoard.getKing(enemyDivision);
        if (coordinatesCanMoveNextTurn.contains(enemyKing.getCoordinate())) {
            return true;
        }

        return false;
    }

    private boolean isCheckmate(ChessBoard chessBoard, PlayChessService.Division enemyDivision) {
        List<Coordinate> coordinatesEnemyCanMove = new ArrayList<>();

        for (ChessPiece enemyPiece : chessBoard.getPieces(enemyDivision)) {
            List<Coordinate> enemyPieceCanMoveCoordinates = findSquareCoordinateCanMove(chessBoard, enemyPiece.getFile(), enemyPiece.getRank());
            for (Coordinate coordinate : coordinatesEnemyCanMove) {
                if (enemyPieceCanMoveCoordinates.contains(coordinate)) {
                    enemyPieceCanMoveCoordinates.remove(coordinate);
                }
            }
            coordinatesEnemyCanMove.addAll(filterKingCanDead(chessBoard, enemyPieceCanMoveCoordinates,  enemyPiece));
        }

        return coordinatesEnemyCanMove.size() == 0;
    }

    public List filterKingCanDead(ChessBoard chessBoard, List<Coordinate> coordinatesCanMove, ChessPiece pieceWillMove) {
        List<Coordinate> filteredCoordinates = new ArrayList<>(coordinatesCanMove);

        King king = chessBoard.getKing(pieceWillMove.division);

        char currentPieceFile = pieceWillMove.getFile();
        int currentPieceRank = pieceWillMove.getRank();
        PlayChessService.Division enemyDivision = pieceWillMove.division == PlayChessService.Division.White? PlayChessService.Division.Black : PlayChessService.Division.White;

        chessBoard.placePiece(currentPieceFile, currentPieceRank, null);
        for (Coordinate coordinate : coordinatesCanMove) {
            ChessPiece pieceAlreadyPlace = chessBoard.getPieceAt(coordinate.getFile(), coordinate.getRank());
            chessBoard.placePiece(coordinate.getFile(), coordinate.getRank(), pieceWillMove);
            if (arePiecesCanMove(chessBoard, enemyDivision, king.getCoordinate())) {
                filteredCoordinates.remove(coordinate);
            }
            chessBoard.placePiece(coordinate.getFile(), coordinate.getRank(), pieceAlreadyPlace);
        }
        chessBoard.placePiece(currentPieceFile, currentPieceRank, pieceWillMove);

        return filteredCoordinates;
    }

    boolean arePiecesCanMove(ChessBoard chessBoard, PlayChessService.Division division, Coordinate coordinate) {
        List<ChessPiece> pieces = chessBoard.getPieces(division);
        for (ChessPiece piece : pieces) {
            List<Coordinate> coordinatesCanMove = findSquareCoordinateCanMove(chessBoard, piece.getFile(), piece.getRank());
            if (coordinatesCanMove.contains(coordinate)) {
                return true;
            }
        }

        return false;
    }
}
