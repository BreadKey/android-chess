package io.github.breadkey.chess.model;

import java.util.List;

import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Coordinate;

public abstract class PlayChessController implements ChessPlayObserver {
    private Coordinate currentSelectedCoordinate;
    private PlayChessService playChessService;
    private PlayerMatcher playerMatcher;
    private Player player;
    private Player enemy;

    public PlayChessController() {
        playChessService = new PlayChessService();
    }

    public void startNewGame() {
        playChessService.addObserver(this);
        playChessService.startNewGame(player, enemy);
    }

    public void select(char file, int rank) {
        if (currentSelectedCoordinate == null) {
            ChessPiece selectedPiece = playChessService.getPieceAt(file, rank);
            if (playChessService.getPieceAt(file, rank) != null) {
                if (selectedPiece.division == playChessService.getCurrentTurn()) {
                    currentSelectedCoordinate = new Coordinate(file, rank);
                }
            }
        }
        else {
            playChessService.tryMove(currentSelectedCoordinate.getFile(), currentSelectedCoordinate.getRank(), file, rank);
            currentSelectedCoordinate = null;
        }
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

    public void setPlayerMatcher(PlayerMatcher playerMatcher) {
        this.playerMatcher = playerMatcher;
    }

    public PlayerMatcher getPlayerMatcher() {
        return playerMatcher;
    }

    public abstract void findEnemy();
}
