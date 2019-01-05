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
            return null;
        }

        List<Coordinate> coordinates = new ArrayList<>();
        ChessGame.Division division = piece.division;

        switch (piece.type) {
            case Pawn: {
                int forward = 1;
                if (division == ChessGame.Division.Black) {
                    forward = -1;
                }
                if (chessBoard.getPieceAt(file, rank + forward) == null) {
                    coordinates.add(new Coordinate(file, rank + forward));
                }
                if (piece.moveCount == 0) {
                    coordinates.add(new Coordinate(file, rank + forward * 2));
                }
            }
        }

        return coordinates;
    }
}
