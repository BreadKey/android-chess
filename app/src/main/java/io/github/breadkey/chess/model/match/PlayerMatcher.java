package io.github.breadkey.chess.model.match;

import io.github.breadkey.chess.model.Player;

public interface PlayerMatcher {
    void startFindEnemy(Player playerWantToPlay);
    Player getEnemy();

    void attachObserver(MatchPlayerObserver observer);

    void gameEnded(Player winnerPlayer);

    boolean getResponseEnemyAcceptPlay();
}
