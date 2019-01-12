package io.github.breadkey.chess.model;

import java.util.Observer;

import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Move;

public interface ChessPlayObserver {
    void divisionDecided(Player whitePlayer, Player blackPlayer);
    void pieceMoved(Move move);
    void canNotMoveThatCoordinates(char fromFile, char toFile, int fromRank, int toRank);
    void moveUndid(Move move);
    void killHappened(ChessPiece pieceWillMove, ChessPiece pieceWillDead, char toFile, int toRank);
}
