package io.github.breadkey.chess.model;

import java.util.List;

import io.github.breadkey.chess.model.chess.ChessRuleManager;
import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Coordinate;
import io.github.breadkey.chess.model.match.MatchPlayerObserver;
import io.github.breadkey.chess.model.match.PlayerMatcher;
import io.github.breadkey.chess.model.match.PlayerMatcherFactory;

public abstract class PlayChessController implements ChessPlayObserver, MatchPlayerObserver {
    private Coordinate currentSelectedCoordinate;
    private PlayChessService playChessService;
    private PlayerMatcher playerMatcher;
    private Player player;
    private Player enemy;

    public PlayChessController() {
        playChessService = new PlayChessService();
        player = new Player("player1");
    }

    public void startNewGame() {
        setEnemy(playerMatcher.getEnemy());
        playChessService.attachGameObserver(this);
        playChessService.startNewGame(player, enemy);
    }

    public void select(char file, int rank) {
        if (isSelectFromCoordinate()) {
            ChessPiece selectedPiece = playChessService.getPieceAt(file, rank);
            if (selectedPiece != null) {
                if (selectedPiece.division == playChessService.getCurrentTurn()) {
                    currentSelectedCoordinate = new Coordinate(file, rank);
                    List<Coordinate> coordinatesPieceCanMove = ChessRuleManager.getInstance().findSquareCoordinateCanMove(getPlayChessService().getChessBoard(), file, rank);
                    coordinatesPieceCanMoveFounded(coordinatesPieceCanMove);
                }
            }
        }
        else {
            ChessPiece selectedPiece = playChessService.getPieceAt(file, rank);
            if (selectedPiece != null) {
                if (selectedPiece.division == playChessService.getCurrentTurn()) {
                    currentSelectedCoordinate = null;
                    select(file, rank);
                    return;
                }
            }
            playChessService.tryMove(currentSelectedCoordinate.getFile(), currentSelectedCoordinate.getRank(), file, rank);
            currentSelectedCoordinate = null;
        }
    }

    public void promote(ChessPiece.Type promoteTo) {
        playChessService.promote(promoteTo);
    }

    private boolean isSelectFromCoordinate() {
        return currentSelectedCoordinate == null;
    }

    public PlayChessService getPlayChessService() {
        return playChessService;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }

    public Player getEnemy() {
        return enemy;
    }

    public void startFindEnemy(PlayerMatcherFactory.PlayerMatcherKey matcherKey) {
        playerMatcher = PlayerMatcherFactory.createPlayerMatcher(matcherKey);
        playerMatcher.attachObserver(this);
        playerMatcher.startFindEnemy(player);
    }

    public PlayerMatcher getPlayerMatcher() {
        return playerMatcher;
    }

    @Override
    public void gameEnded(PlayChessService.Division winner) {
        playerMatcher.gameEnded(playChessService.getCurrentPlayer());
    }

    @Override
    public void enemyFounded(PlayerMatcherFactory.PlayerMatcherKey key) {
        switch (key) {
            case VsInReal:
                setEnemy(playerMatcher.getEnemy());
                startNewGame();
                break;
            case VsOnline:
                if (requestPlayerAcceptPlay()) {
                    if (playerMatcher.getResponseEnemyAcceptPlay()) {
                        setEnemy(playerMatcher.getEnemy());
                        startNewGame();
                        break;
                    }
                }
        }
    }
    public abstract void coordinatesPieceCanMoveFounded(List<Coordinate> coordinates);
    public abstract boolean requestPlayerAcceptPlay();
    public abstract void turnChanged(PlayChessService.Division turn);
}
