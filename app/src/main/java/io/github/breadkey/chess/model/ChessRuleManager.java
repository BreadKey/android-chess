package io.github.breadkey.chess.model;

import java.util.ArrayList;
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
}
