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
                int pieceFileIndex = chessBoard.files.indexOf(file);
                int fileCount = ChessBoard.files.size();
                List<Character> leftFiles = new ArrayList<>(ChessBoard.files.subList(0, pieceFileIndex));
                Collections.reverse(leftFiles);
                List<Coordinate> leftCoordinates = new ArrayList<>();
                for (char currentFile: leftFiles) {
                    leftCoordinates.add(new Coordinate(currentFile, rank));
                }
                findCoordinatesInCandidates(chessBoard, coordinates, leftCoordinates, piece.division);

                List<Character> rightFiles = ChessBoard.files.subList(pieceFileIndex + 1, fileCount);
                List<Coordinate> rightCoordinates = new ArrayList<>();
                for (char currentFile: rightFiles) {
                    rightCoordinates.add(new Coordinate(currentFile, rank));
                }
                findCoordinatesInCandidates(chessBoard, coordinates, rightCoordinates, piece.division);

                int pieceRankIndex = ChessBoard.ranks.indexOf(rank);
                int rankCount = ChessBoard.ranks.size();
                List<Integer> upRanks = new ArrayList<>(ChessBoard.ranks.subList(0, pieceRankIndex));
                Collections.reverse(upRanks);
                List<Coordinate> upCoordinates = new ArrayList<>();
                for (int currentRank: upRanks) {
                    upCoordinates.add(new Coordinate(file, currentRank));
                }
                findCoordinatesInCandidates(chessBoard, coordinates, upCoordinates, piece.division);

                List<Integer> downRanks = ChessBoard.ranks.subList(pieceRankIndex + 1, rankCount);
                List<Coordinate> downCoordinates = new ArrayList<>();
                for (int currentRank: downRanks) {
                    downCoordinates.add(new Coordinate(file, currentRank));
                }
                findCoordinatesInCandidates(chessBoard, coordinates, downCoordinates, piece.division);
            }
        }

        return coordinates;
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
