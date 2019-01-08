package io.github.breadkey.chess.model;

import io.github.breadkey.chess.model.chess.ChessGame;

class TestChessGameManager extends ChessGameManager {
    private Player mainPlayer;

    @Override
    void divisionDecided(Player whitePlayer, Player blackPlayer) {
        System.out.println("백: " + whitePlayer.nickName);
        System.out.println("흑: " + blackPlayer.nickName);
    }

    @Override
    void pieceMoveAction(Move move) {
        System.out.print(getChessGame());
        System.out.println(" " + getCurrentPlayer().nickName);
    }

    @Override
    void canNotMoveAction(char fromFile, int fromRank, char toFile, int toRank) {

    }

    @Override
    void undoMoveAction() {

    }
}
