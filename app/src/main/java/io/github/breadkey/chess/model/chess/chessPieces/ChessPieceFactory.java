package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.ChessPiece;

public class ChessPieceFactory {
    public static ChessPiece promotion(Pawn pawn, ChessPiece.Type promoteTo) {
        ChessPiece promotedPiece;

        switch (promoteTo) {
            case Knight:
                promotedPiece = new Knight(pawn.division);
                break;
            case Rook:
                promotedPiece = new Rook(pawn.division);
                break;
            case Bishop:
                promotedPiece = new Bishop(pawn.division);
                break;
            default:
                promotedPiece = new Queen(pawn.division);
                break;
        }

        copyStatus(pawn, promotedPiece);

        return promotedPiece;
    }

    public static Pawn demote(ChessPiece piece) {
        Pawn pawn = new Pawn(piece.division);
        copyStatus(piece, pawn);

        return pawn;
    }

    private static void copyStatus(ChessPiece from, ChessPiece to) {
        to.setCoordinate(from.getFile(), from.getRank());
        to.killScore = from.killScore;
        to.moveCount = from.moveCount;
    }
}
