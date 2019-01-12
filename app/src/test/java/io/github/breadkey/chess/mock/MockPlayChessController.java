package io.github.breadkey.chess.mock;

import io.github.breadkey.chess.model.PlayChessController;
import io.github.breadkey.chess.model.Player;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.model.chess.PlayChessService;

public class MockPlayChessController extends PlayChessController {
    @Override
    public void divisionDecided(Player whitePlayer, Player blackPlayer) {
        System.out.println("백: " + whitePlayer.nickName);
        System.out.println("흑: " + blackPlayer.nickName);
    }

    @Override
    public void pieceMoved(Move move) {
        printGameStatus();
    }

    @Override
    public void canNotMoveThatCoordinates(char fromFile, char toFile, int fromRank, int toRank) {
        printGameStatus();
    }

    @Override
    public void moveUndid(Move move) {
        printGameStatus();
    }

    @Override
    public void killHappened(ChessPiece pieceWillMove, ChessPiece pieceWillDead, char toFile, int toRank) {
        printGameStatus();
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

    @Override
    public void findEnemy() {
        getPlayerMatcher().startFindEnemy(getPlayer());
        setEnemy(getPlayerMatcher().getEnemy());
    }
}
