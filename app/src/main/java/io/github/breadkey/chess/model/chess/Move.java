package io.github.breadkey.chess.model.chess;

import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Coordinate;

public class Move {
    private PlayChessService.Division division;
    private ChessPiece.Type pieceType;
    private Coordinate fromCoordinate;
    private Coordinate toCoordinate;

    public Move(PlayChessService.Division division, ChessPiece.Type pieceType, Coordinate fromCoordinate, Coordinate toCoordinate) {
        this.division = division;
        this.pieceType = pieceType;
        this.fromCoordinate = fromCoordinate;
        this.toCoordinate = toCoordinate;
    }

    public PlayChessService.Division getDivision() {
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (pieceType == ChessPiece.Type.Pawn) {
        }
        else if (pieceType == ChessPiece.Type.Knight) {
            stringBuilder.append('N');
        }
        else {
            stringBuilder.append(pieceType.toString().charAt(0));
        }
        stringBuilder.append(toCoordinate.getFile());
        stringBuilder.append(toCoordinate.getRank());
        return stringBuilder.toString();
    }
}
