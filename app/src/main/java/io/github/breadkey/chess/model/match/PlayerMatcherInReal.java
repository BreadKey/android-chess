package io.github.breadkey.chess.model.match;

import io.github.breadkey.chess.model.Player;

public class PlayerMatcherInReal implements PlayerMatcher {
    private MatchPlayerObserver observer;

    @Override
    public void startFindEnemy(Player playerWantToPlay) {
    observer.enemyFounded(PlayerMatcherFactory.PlayerMatcherKey.VsInReal);
    }

    @Override
    public Player getEnemy() {
    return new Player("player2");
    }

    @Override
    public void attachObserver(MatchPlayerObserver observer) {
    this.observer = observer;
    }

    @Override
    public void gameEnded(Player winnerPlayer) {

    }

    @Override
    public boolean getResponseEnemyAcceptPlay() {
        return true;
    }
}
