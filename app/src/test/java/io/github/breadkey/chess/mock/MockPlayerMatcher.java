package io.github.breadkey.chess.mock;

import io.github.breadkey.chess.model.Player;
import io.github.breadkey.chess.model.match.MatchPlayerObserver;
import io.github.breadkey.chess.model.match.PlayerMatcher;

public class MockPlayerMatcher implements PlayerMatcher {
    private Player enemy;
    private MatchPlayerObserver observer;

    @Override
    public void startFindEnemy(Player playerWantToPlay) {
        enemy = new MockPlayerHJ();
    }

    @Override
    public Player getEnemy() {
        return enemy;
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
