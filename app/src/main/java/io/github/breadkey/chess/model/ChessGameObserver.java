package io.github.breadkey.chess.model;

import java.util.Observer;

import io.github.breadkey.chess.model.chess.ChessPiece;

public interface ChessGameObserver {
    abstract void pieceMoved(char fromFile, int fromRank, char toFile, int toRank, ChessPiece movedPiece);

    abstract void canNotMoveThatCoordinates(char fromFile, char toFile, int fromRank, int toRank);

    void killHappened(ChessPiece pieceWillMove, ChessPiece pieceWillDead, char toFile, int toRank);
}
