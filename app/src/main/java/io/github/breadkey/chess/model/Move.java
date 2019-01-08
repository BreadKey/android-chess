package io.github.breadkey.chess.model;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Coordinate;

public class Move {
    private ChessGame.Division division;
    private ChessPiece.Type pieceType;
    private Coordinate fromCoordinate;
    private Coordinate toCoordinate;

    public Move(ChessGame.Division division, ChessPiece.Type pieceType, Coordinate fromCoordinate, Coordinate toCoordinate) {
        this.division = division;
        this.pieceType = pieceType;
        this.fromCoordinate = fromCoordinate;
        this.toCoordinate = toCoordinate;
    }

    public ChessGame.Division getDivision() {
        return division;
    }

    public ChessPiece.Type getPieceType() {
        return pieceType;
    }

    public Coordinate getFromCoordinate() {
        return fromCoordinate;
    }

    public Coordinate getToCoordinate() {
        return toCoordinate;
    }
}
