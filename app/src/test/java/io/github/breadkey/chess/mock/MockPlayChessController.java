package io.github.breadkey.chess.mock;

import java.util.List;

import io.github.breadkey.chess.model.PlayChessController;
import io.github.breadkey.chess.model.Player;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Coordinate;
import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.match.PlayerMatcherFactory;

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

    @Override
    public void gameEnded(PlayChessService.Division winner) {

    }

    private void printGameStatus() {
        PlayChessService playChessService = getPlayChessService();
        System.out.print(playChessService);
        System.out.println(" " + playChessService.getCurrentPlayer().nickName);
        for (int moveIndex = 1; moveIndex < playChessService.getMoves().size(); moveIndex += 2) {
            Move whiteMove = playChessService.getMoves().get(moveIndex - 1);
            Move blackMove = playChessService.getMoves().get(moveIndex);
            System.out.println(String.valueOf(moveIndex / 2 + 1) + ". " + whiteMove + "\t" + blackMove);
        }
    }

    @Override
    public void enemyFounded(PlayerMatcherFactory.PlayerMatcherKey key) {
    }

    @Override
    public void coordinatesPieceCanMoveFounded(List<Coordinate> coordinates) {

    }

    @Override
    public boolean requestPlayerAcceptPlay() {
        return true;
    }

    @Override
    public void turnChanged(PlayChessService.Division turn) {

    }

    @Override
    public void selectTypeToPromotion() {

    }
}
