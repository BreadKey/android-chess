package io.github.breadkey.chess.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChessRuleManager {
    private static final ChessRuleManager ourInstance = new ChessRuleManager();

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
        ChessGame.Division division = piece.division;

        switch (piece.type) {
            case Pawn: {
                int forward = 1;
                if (division == ChessGame.Division.Black) {
                    forward = -1;
                }
                int forwardRank = rank + forward;
                if (ChessBoard.ranks.contains(forwardRank)) {
                    if (chessBoard.getPieceAt(file, forwardRank) == null) {
                        coordinates.add(new Coordinate(file, forwardRank));

                        if (piece.moveCount == 0) {
                            coordinates.add(new Coordinate(file, forwardRank + forward));
                        }
                    }

                    List<Character> sideFiles = new ArrayList<>();
                    char leftFile = (char) (file - 1);
                    char rightFile = (char) (file + 1);
                    if (ChessBoard.files.contains(leftFile)) {
                        sideFiles.add(leftFile);
                    }
                    if (ChessBoard.files.contains(rightFile)) {
                        sideFiles.add(rightFile);
                    }

                    for (char sideFile : sideFiles) {
                        if (isEnemyPlaced(chessBoard, piece.division, sideFile, forwardRank)) {
                            coordinates.add(new Coordinate(sideFile, forwardRank));
                        }
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
                List<Coordinate> filteredCoordinates = new ArrayList<>(coordinates);

                for (Coordinate coordinate: coordinates) {
                    if (chessBoard.isOutOfBoard(coordinate.getFile(), coordinate.getRank())) {
                        filteredCoordinates.remove(coordinate);
                    }
                }

                coordinates = new ArrayList<>(filteredCoordinates);
                for (Coordinate coordinate: coordinates) {
                    ChessPiece pieceOnSquare = chessBoard.getPieceAt(coordinate.getFile(), coordinate.getRank());
                    if (pieceOnSquare != null) {
                        if (pieceOnSquare.division == piece.division) {
                            filteredCoordinates.remove(coordinate);
                        }
                    }
                }

                coordinates = filteredCoordinates;
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
        }

        return coordinates;
    }

    private void findRookCoordinatesCanMove(ChessBoard chessBoard, List<Coordinate> destination, char file, int rank, ChessGame.Division division) {
        int pieceFileIndex = ChessBoard.files.indexOf(file);
        int fileCount = ChessBoard.files.size();
        int pieceRankIndex = ChessBoard.ranks.indexOf(rank);
        int rankCount = ChessBoard.ranks.size();

        List<Coordinate> leftCoordinates = new ArrayList<>();
        for (char leftFile: ChessBoard.files.subList(0, pieceFileIndex)) {
            leftCoordinates.add(new Coordinate(leftFile, rank));
        }
        Collections.reverse(leftCoordinates);
        findCoordinatesInCandidates(chessBoard, destination, leftCoordinates, division);

        List<Coordinate> rightCoordinates = new ArrayList<>();
        for (char rightFile: ChessBoard.files.subList(pieceFileIndex + 1, fileCount)) {
            rightCoordinates.add(new Coordinate(rightFile, rank));
        }
        findCoordinatesInCandidates(chessBoard, destination, rightCoordinates, division);

        List<Coordinate> upCoordinates = new ArrayList<>();
        for (int upRank: ChessBoard.ranks.subList(0, pieceRankIndex)) {
            upCoordinates.add(new Coordinate(file, upRank));
        }
        Collections.reverse(upCoordinates);
        findCoordinatesInCandidates(chessBoard, destination, upCoordinates, division);

        List<Coordinate> downCoordinates = new ArrayList<>();
        for (int downRank: ChessBoard.ranks.subList(pieceRankIndex + 1, rankCount)) {
            downCoordinates.add(new Coordinate(file, downRank));
        }
        findCoordinatesInCandidates(chessBoard, destination, downCoordinates, division);
    }

    private void findBishopCoordinatesCanMove(ChessBoard chessBoard, List<Coordinate> destination, char file, int rank, ChessGame.Division division) {
        int left = -1;
        int right = 1;
        int up = 1;
        int down = -1;

        List<Coordinate> leftUpCoordinates = findDiagonalCoordinates(file, rank, left , up);
        findCoordinatesInCandidates(chessBoard, destination, leftUpCoordinates, division);

        List<Coordinate> rightDownCoordinates = findDiagonalCoordinates(file, rank, right, down);
        findCoordinatesInCandidates(chessBoard, destination, rightDownCoordinates, division);

        List<Coordinate> leftDownCoordinates = findDiagonalCoordinates(file, rank, left, down);
        findCoordinatesInCandidates(chessBoard, destination, leftDownCoordinates, division);

        List<Coordinate> rightUpCoordinates = findDiagonalCoordinates(file, rank, right, up);
        findCoordinatesInCandidates(chessBoard, destination, rightUpCoordinates, division);
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

    private boolean isEnemyPlaced(ChessBoard chessBoard, ChessGame.Division pieceDivision, char file, int rank) {
        ChessPiece pieceWillEnemy = chessBoard.getPieceAt(file, rank);
        if (pieceWillEnemy != null) {
            return pieceWillEnemy.division != pieceDivision;
        }

        return false;
    }

    private void findCoordinatesInCandidates(ChessBoard chessBoard, List<Coordinate> destination, List<Coordinate> candidates, ChessGame.Division division) {
        for (Coordinate coordinate: candidates) {
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
}
