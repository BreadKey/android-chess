package io.github.breadkey.chess.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private PlayChessService.Division division;
    private ChessPiece.Type pieceType;
    private Coordinate fromCoordinate;
    private Coordinate toCoordinate;
    private List<ChessRuleManager.Rule> rules;
    private ChessPiece.Type promotedType;

    public Move(PlayChessService.Division division, ChessPiece.Type pieceType, Coordinate fromCoordinate, Coordinate toCoordinate) {
        this.division = division;
        this.pieceType = pieceType;
        this.fromCoordinate = fromCoordinate;
        this.toCoordinate = toCoordinate;
        rules = new ArrayList<>();
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

    public List<ChessRuleManager.Rule> getRules() {
        return rules;
    }

    public void setRules(List<ChessRuleManager.Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(makePieceInitial(pieceType));
        stringBuilder.append(toCoordinate.getFile());
        stringBuilder.append(toCoordinate.getRank());
        for (ChessRuleManager.Rule rule : rules) {
            switch (rule) {
                case KingSideCastling:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("0-0");
                    break;
                case QueenSideCastling:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("0-0-0");
                    break;
                case Check:
                    stringBuilder.append('+');
                    break;
                case Checkmate:
                    stringBuilder.append('#');
                    break;
                case Promotion:
                    stringBuilder.append("=" + makePieceInitial(promotedType));
            }
        }

        return stringBuilder.toString();
    }

    private String makePieceInitial(ChessPiece.Type pieceType) {
        if (pieceType != ChessPiece.Type.Pawn) {
            if (pieceType == ChessPiece.Type.Knight) {
                return "N";
            }
            else {
                return String.valueOf(pieceType.toString().charAt(0));
            }
        }

        return "";
    }

    public void setPromotedType(ChessPiece.Type promotedType) {
        this.promotedType = promotedType;
    }
}
