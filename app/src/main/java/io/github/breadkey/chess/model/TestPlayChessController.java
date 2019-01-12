package io.github.breadkey.chess.model;

import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.model.chess.PlayChessService;

class TestPlayChessController extends PlayChessController {
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
        PlayChessService playChessService = getPlayChessService();
        System.out.print(playChessService);
        System.out.println(" " + playChessService.getCurrentPlayer().nickName);
        for (int moveIndex = 1; moveIndex < playChessService.getMoves().size(); moveIndex += 2) {
            Move whiteMove = playChessService.getMoves().get(moveIndex - 1);
            Move blackMove = playChessService.getMoves().get(moveIndex);
            System.out.println(String.valueOf(moveIndex % 2) + ". " + whiteMove + "\t" + blackMove);
        }
    }
}
