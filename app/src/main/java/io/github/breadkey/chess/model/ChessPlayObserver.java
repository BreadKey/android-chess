package io.github.breadkey.chess.model;

import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.model.chess.PlayChessService;

public interface ChessPlayObserver {
    void divisionDecided(Player whitePlayer, Player blackPlayer);
    void pieceMoved(Move move);
    void canNotMoveThatCoordinates(char fromFile, char toFile, int fromRank, int toRank);
    void moveUndid(Move move);
    void killHappened(ChessPiece pieceWillMove, ChessPiece pieceWillDead, char toFile, int toRank);
    void gameEnded(PlayChessService.Division winner);
}
