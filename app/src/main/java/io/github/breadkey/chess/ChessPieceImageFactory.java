package io.github.breadkey.chess;

import android.widget.Button;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class ChessPieceImageFactory {
    public static int createPieceImage(ChessPiece piece) {
        ChessGame.Division division = piece.division;

        switch (piece.type) {
            case Pawn: {
                if (division == ChessGame.Division.White) {
                    return R.drawable.pawn_default_white;
                }
                else {
                    return R.drawable.pawn_default_black;
                }
            }
            case Rook: {
                if (division == ChessGame.Division.White) {
                    return R.drawable.rook_default_white;
                }
                else {
                    return R.drawable.rook_default_black;
                }
            }

            case Knight: {
                if (division == ChessGame.Division.White) {
                    return R.drawable.knight_default_white;
                }
                else {
                    return R.drawable.knight_default_black;
                }
            }

            case Bishop: {
                if (division == ChessGame.Division.White) {
                    return R.drawable.bishop_default_white;
                }
                else {
                    return R.drawable.bishop_default_black;
                }
            }

            case Queen: {
                if (division == ChessGame.Division.White) {
                    return R.drawable.queen_default_white;
                }
                else {
                    return R.drawable.queen_default_black;
                }
            }

            case King: {
                if (division == ChessGame.Division.White) {
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
