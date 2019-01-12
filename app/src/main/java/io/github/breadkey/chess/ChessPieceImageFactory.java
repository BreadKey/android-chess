package io.github.breadkey.chess;

import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class ChessPieceImageFactory {
    public static int createPieceImage(ChessPiece piece) {
        PlayChessService.Division division = piece.division;

        switch (piece.type) {
            case Pawn: {
                if (division == PlayChessService.Division.White) {
                    return R.drawable.pawn_default_white;
                }
                else {
                    return R.drawable.pawn_default_black;
                }
            }
            case Rook: {
                if (division == PlayChessService.Division.White) {
                    return R.drawable.rook_default_white;
                }
                else {
                    return R.drawable.rook_default_black;
                }
            }

            case Knight: {
                if (division == PlayChessService.Division.White) {
                    return R.drawable.knight_default_white;
                }
                else {
                    return R.drawable.knight_default_black;
                }
            }

            case Bishop: {
                if (division == PlayChessService.Division.White) {
                    return R.drawable.bishop_default_white;
                }
                else {
                    return R.drawable.bishop_default_black;
                }
            }

            case Queen: {
                if (division == PlayChessService.Division.White) {
                    return R.drawable.queen_default_white;
                }
                else {
                    return R.drawable.queen_default_black;
                }
            }

            case King: {
                if (division == PlayChessService.Division.White) {
                    return R.drawable.king_default_white;
                }
                else {
                    return R.drawable.king_default_black;
                }
            }
        }

        return R.drawable.pawn_default_white;
    }
}
