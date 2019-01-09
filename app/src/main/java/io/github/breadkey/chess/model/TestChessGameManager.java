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
        printGameStatus();
    }

    @Override
    void canNotMoveAction(char fromFile, int fromRank, char toFile, int toRank) {
        printGameStatus();
    }

    @Override
    void undoMoveAction() {

    }

    private void printGameStatus() {
        System.out.print(getChessGame());
        System.out.println(" " + getCurrentPlayer().nickName);
        for (int moveIndex = 1; moveIndex < getMoves().size(); moveIndex += 2) {
            Move whiteMove = getMoves().get(moveIndex - 1);
            Move blackMove = getMoves().get(moveIndex);
            System.out.println(String.valueOf(moveIndex % 2) + ". " + whiteMove + "\t" + blackMove);
        }
    }
}
